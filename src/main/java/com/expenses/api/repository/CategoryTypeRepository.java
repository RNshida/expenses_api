package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {
    List<CategoryType> findByUserOrderByDisplayOrderAscNameAsc(AppUser user);
    Optional<CategoryType> findByIdAndUser(Long id, AppUser user);
    boolean existsByUserAndName(AppUser user, String name);
    boolean existsByUserAndNameAndIdNot(AppUser user, String name, Long id);
}
