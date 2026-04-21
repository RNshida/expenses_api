package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.CategoryGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryGoalRepository extends JpaRepository<CategoryGoal, Long> {

    List<CategoryGoal> findByUser(AppUser user);

    Optional<CategoryGoal> findByUserAndCategoryId(AppUser user, Long categoryId);

    void deleteByUserAndCategoryId(AppUser user, Long categoryId);
}
