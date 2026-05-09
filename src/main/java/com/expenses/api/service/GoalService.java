package com.expenses.api.service;

import com.expenses.api.dto.CategoryTypeGoalItemDto;
import com.expenses.api.dto.CategoryTypeGoalSaveRequest;
import com.expenses.api.dto.GoalItemDto;
import com.expenses.api.dto.GoalSaveRequest;
import com.expenses.api.dto.TypeGoalPeriodsDto;
import com.expenses.api.dto.TypeGoalPeriodSaveRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.Category;
import com.expenses.api.entity.CategoryGoal;
import com.expenses.api.entity.CategoryType;
import com.expenses.api.entity.CategoryTypeGoal;
import com.expenses.api.entity.CategoryTypeGoalPeriod;
import com.expenses.api.repository.CategoryGoalRepository;
import com.expenses.api.repository.CategoryRepository;
import com.expenses.api.repository.CategoryTypeGoalPeriodRepository;
import com.expenses.api.repository.CategoryTypeGoalRepository;
import com.expenses.api.repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final CategoryGoalRepository goalRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeGoalRepository typeGoalRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryTypeGoalPeriodRepository periodRepository;

    @Transactional(readOnly = true)
    public List<GoalItemDto> getGoals(AppUser user) {
        List<Category> categories = categoryRepository.findByUserOrderByDisplayOrderAscNameAsc(user);
        Map<Long, CategoryGoal> goalMap = goalRepository.findByUser(user)
                .stream()
                .collect(Collectors.toMap(g -> g.getCategory().getId(), g -> g));

        return categories.stream()
                .map(c -> new GoalItemDto(
                        c.getId(),
                        c.getName(),
                        goalMap.containsKey(c.getId()) ? goalMap.get(c.getId()).getTargetAmount() : null
                ))
                .toList();
    }

    @Transactional
    public void saveGoals(AppUser user, GoalSaveRequest request) {
        for (GoalSaveRequest.GoalItem item : request.getGoals()) {
            Category category = categoryRepository.findByIdAndUser(item.getCategoryId(), user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "カテゴリが見つかりません"));

            if (item.getTargetAmount() == null) {
                goalRepository.deleteByUserAndCategoryId(user, item.getCategoryId());
            } else {
                CategoryGoal goal = goalRepository
                        .findByUserAndCategoryId(user, item.getCategoryId())
                        .orElse(new CategoryGoal(user, category, item.getTargetAmount()));
                goal.setTargetAmount(item.getTargetAmount());
                goalRepository.save(goal);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryTypeGoalItemDto> getTypeGoals(AppUser user) {
        List<CategoryType> types = categoryTypeRepository.findByUserOrderByDisplayOrderAscNameAsc(user);
        Map<Long, CategoryTypeGoal> goalMap = typeGoalRepository.findByUser(user)
                .stream()
                .collect(Collectors.toMap(g -> g.getCategoryType().getId(), g -> g));

        return types.stream()
                .map(t -> new CategoryTypeGoalItemDto(
                        t.getId(),
                        t.getName(),
                        goalMap.containsKey(t.getId()) ? goalMap.get(t.getId()).getTargetAmount() : null
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TypeGoalPeriodsDto> getTypeGoalPeriods(AppUser user) {
        List<CategoryType> types = categoryTypeRepository.findByUserOrderByDisplayOrderAscNameAsc(user);
        List<CategoryTypeGoalPeriod> allPeriods = periodRepository.findByUser(user);

        return types.stream()
                .map(t -> {
                    List<TypeGoalPeriodsDto.PeriodItem> periods = allPeriods.stream()
                            .filter(p -> p.getCategoryType().getId().equals(t.getId()))
                            .sorted(Comparator.comparing(CategoryTypeGoalPeriod::getStartYearMonth))
                            .map(p -> new TypeGoalPeriodsDto.PeriodItem(
                                    p.getId(), p.getStartYearMonth(), p.getEndYearMonth(), p.getTargetAmount()))
                            .toList();
                    return new TypeGoalPeriodsDto(t.getId(), t.getName(), periods);
                })
                .toList();
    }

    @Transactional
    public void saveTypeGoalPeriods(AppUser user, Long categoryTypeId, TypeGoalPeriodSaveRequest request) {
        CategoryType type = categoryTypeRepository.findByIdAndUser(categoryTypeId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "種別が見つかりません"));

        periodRepository.deleteByUserAndCategoryTypeId(user, categoryTypeId);

        if (request.getPeriods() != null) {
            for (TypeGoalPeriodSaveRequest.PeriodItem item : request.getPeriods()) {
                periodRepository.save(new CategoryTypeGoalPeriod(
                        user, type, item.getStartYearMonth(), item.getEndYearMonth(), item.getTargetAmount()));
            }
        }
    }

    @Transactional
    public void saveTypeGoals(AppUser user, CategoryTypeGoalSaveRequest request) {
        for (CategoryTypeGoalSaveRequest.GoalItem item : request.getGoals()) {
            CategoryType type = categoryTypeRepository.findByIdAndUser(item.getCategoryTypeId(), user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "種別が見つかりません"));

            if (item.getTargetAmount() == null) {
                typeGoalRepository.deleteByUserAndCategoryTypeId(user, item.getCategoryTypeId());
            } else {
                CategoryTypeGoal goal = typeGoalRepository
                        .findByUserAndCategoryTypeId(user, item.getCategoryTypeId())
                        .orElse(new CategoryTypeGoal(user, type, item.getTargetAmount()));
                goal.setTargetAmount(item.getTargetAmount());
                typeGoalRepository.save(goal);
            }
        }
    }
}
