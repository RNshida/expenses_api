package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.CategoryTypeGoalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryTypeGoalPeriodRepository extends JpaRepository<CategoryTypeGoalPeriod, Long> {
    List<CategoryTypeGoalPeriod> findByUser(AppUser user);
    void deleteByUserAndCategoryTypeId(AppUser user, Long categoryTypeId);
}
