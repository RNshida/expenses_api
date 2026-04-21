package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserOrderByDisplayOrderAscNameAsc(AppUser user);

    boolean existsByUserAndName(AppUser user, String name);

    boolean existsByUserAndNameAndIdNot(AppUser user, String name, Long id);

    Optional<Category> findByIdAndUser(Long id, AppUser user);
}
