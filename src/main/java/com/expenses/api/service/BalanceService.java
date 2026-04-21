package com.expenses.api.service;

import com.expenses.api.dto.*;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.Category;
import com.expenses.api.entity.MonthlyBalance;
import com.expenses.api.repository.CategoryRepository;
import com.expenses.api.repository.MonthlyBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private static final DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    private final CategoryRepository categoryRepository;
    private final MonthlyBalanceRepository balanceRepository;

    @Transactional(readOnly = true)
    public BalanceSummaryResponse getSummary(AppUser user, int months) {
        LocalDate toDate = YearMonth.now().atDay(1);
        LocalDate fromDate = toDate.minusMonths(months - 1L);

        List<String> monthLabels = new ArrayList<>();
        LocalDate cursor = fromDate;
        while (!cursor.isAfter(toDate)) {
            monthLabels.add(cursor.format(YEAR_MONTH_FMT));
            cursor = cursor.plusMonths(1);
        }

        List<MonthlyBalance> records = balanceRepository.findByUserAndYearMonthRange(user, fromDate, toDate);

        Map<Long, Map<String, BigDecimal>> dataMap = new HashMap<>();
        for (MonthlyBalance mb : records) {
            dataMap.computeIfAbsent(mb.getCategory().getId(), k -> new HashMap<>())
                   .put(mb.getYearMonth().format(YEAR_MONTH_FMT), mb.getAmount());
        }

        List<Category> orderedCategories = categoryRepository.findByUserOrderByDisplayOrderAscNameAsc(user);
        List<BalanceSummarySeriesDto> series = new ArrayList<>();

        for (Category cat : orderedCategories) {
            Map<String, BigDecimal> monthData = dataMap.get(cat.getId());
            if (monthData == null) continue;
            List<BigDecimal> amounts = monthLabels.stream()
                    .map(m -> monthData.getOrDefault(m, null))
                    .toList();
            series.add(new BalanceSummarySeriesDto(cat.getId(), cat.getName(), amounts));
        }

        List<BigDecimal> totals = monthLabels.stream().map(month -> {
            BigDecimal sum = null;
            for (Map<String, BigDecimal> monthData : dataMap.values()) {
                BigDecimal val = monthData.get(month);
                if (val != null) {
                    sum = (sum == null) ? val : sum.add(val);
                }
            }
            return sum;
        }).toList();

        series.add(new BalanceSummarySeriesDto(0L, "合計", totals));

        return new BalanceSummaryResponse(monthLabels, series);
    }

    @Transactional(readOnly = true)
    public List<BalanceInputItemDto> getInputBalances(AppUser user, String yearMonthStr) {
        LocalDate yearMonth = parseYearMonth(yearMonthStr);
        List<Category> categories = categoryRepository.findByUserOrderByDisplayOrderAscNameAsc(user);
        List<MonthlyBalance> saved = balanceRepository.findByUserAndYearMonth(user, yearMonth);

        Map<Long, BigDecimal> savedMap = new HashMap<>();
        Map<Long, String> memoMap = new HashMap<>();
        for (MonthlyBalance mb : saved) {
            savedMap.put(mb.getCategory().getId(), mb.getAmount());
            memoMap.put(mb.getCategory().getId(), mb.getMemo());
        }

        return categories.stream()
                .map(c -> new BalanceInputItemDto(c.getId(), c.getName(), savedMap.get(c.getId()), memoMap.get(c.getId())))
                .toList();
    }

    @Transactional
    public void saveBalances(AppUser user, BalanceSaveRequest request) {
        LocalDate yearMonth = parseYearMonth(request.getYearMonth());

        for (BalanceSaveRequest.BalanceItem item : request.getBalances()) {
            Category category = categoryRepository.findByIdAndUser(item.getCategoryId(), user)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "カテゴリが見つかりません: id=" + item.getCategoryId()));

            Optional<MonthlyBalance> existing =
                    balanceRepository.findByCategoryIdAndYearMonth(item.getCategoryId(), yearMonth);

            if (item.getAmount() == null) {
                existing.ifPresent(balanceRepository::delete);
            } else {
                MonthlyBalance balance = existing.orElse(new MonthlyBalance(category, yearMonth, item.getAmount()));
                balance.setAmount(item.getAmount());
                balance.setMemo(item.getMemo());
                balanceRepository.save(balance);
            }
        }
    }

    private LocalDate parseYearMonth(String yearMonthStr) {
        try {
            return YearMonth.parse(yearMonthStr, YEAR_MONTH_FMT).atDay(1);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "年月はYYYY-MM形式で指定してください: " + yearMonthStr);
        }
    }
}
