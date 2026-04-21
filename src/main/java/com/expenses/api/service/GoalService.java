package com.expenses.api.service;

import com.expenses.api.dto.GoalItemDto;
import com.expenses.api.dto.GoalSaveRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.Category;
import com.expenses.api.entity.CategoryGoal;
import com.expenses.api.repository.CategoryGoalRepository;
import com.expenses.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final CategoryGoalRepository goalRepository;
    private final CategoryRepository categoryRepository;

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
}
