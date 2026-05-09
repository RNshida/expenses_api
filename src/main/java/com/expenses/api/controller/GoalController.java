package com.expenses.api.controller;

import com.expenses.api.dto.CategoryTypeGoalItemDto;
import com.expenses.api.dto.CategoryTypeGoalSaveRequest;
import com.expenses.api.dto.GoalItemDto;
import com.expenses.api.dto.GoalSaveRequest;
import com.expenses.api.dto.TypeGoalPeriodsDto;
import com.expenses.api.dto.TypeGoalPeriodSaveRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final AppUserRepository userRepository;

    @GetMapping
    public List<GoalItemDto> getGoals(Principal principal) {
        return goalService.getGoals(currentUser(principal));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveGoals(Principal principal, @RequestBody GoalSaveRequest request) {
        goalService.saveGoals(currentUser(principal), request);
    }

    @GetMapping("/types")
    public List<CategoryTypeGoalItemDto> getTypeGoals(Principal principal) {
        return goalService.getTypeGoals(currentUser(principal));
    }

    @PutMapping("/types")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveTypeGoals(Principal principal, @RequestBody CategoryTypeGoalSaveRequest request) {
        goalService.saveTypeGoals(currentUser(principal), request);
    }

    @GetMapping("/type-periods")
    public List<TypeGoalPeriodsDto> getTypeGoalPeriods(Principal principal) {
        return goalService.getTypeGoalPeriods(currentUser(principal));
    }

    @PutMapping("/type-periods/{categoryTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveTypeGoalPeriods(Principal principal,
                                    @PathVariable Long categoryTypeId,
                                    @RequestBody TypeGoalPeriodSaveRequest request) {
        goalService.saveTypeGoalPeriods(currentUser(principal), categoryTypeId, request);
    }

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが見つかりません"));
    }
}
