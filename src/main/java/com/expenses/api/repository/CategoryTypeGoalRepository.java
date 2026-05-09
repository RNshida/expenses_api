package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.CategoryTypeGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryTypeGoalRepository extends JpaRepository<CategoryTypeGoal, Long> {
    List<CategoryTypeGoal> findByUser(AppUser user);
    Optional<CategoryTypeGoal> findByUserAndCategoryTypeId(AppUser user, Long categoryTypeId);
    void deleteByUserAndCategoryTypeId(AppUser user, Long categoryTypeId);
}
