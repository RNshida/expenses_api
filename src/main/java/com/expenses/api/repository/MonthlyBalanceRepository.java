package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.MonthlyBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyBalanceRepository extends JpaRepository<MonthlyBalance, Long> {

    Optional<MonthlyBalance> findByCategoryIdAndYearMonth(Long categoryId, LocalDate yearMonth);

    void deleteByCategoryId(Long categoryId);

    @Query("SELECT mb FROM MonthlyBalance mb JOIN FETCH mb.category c " +
           "WHERE c.user = :user AND mb.yearMonth >= :from AND mb.yearMonth <= :to " +
           "ORDER BY mb.yearMonth ASC, c.displayOrder ASC, c.name ASC")
    List<MonthlyBalance> findByUserAndYearMonthRange(
            @Param("user") AppUser user,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("SELECT mb FROM MonthlyBalance mb JOIN FETCH mb.category c " +
           "WHERE c.user = :user AND mb.yearMonth = :yearMonth " +
           "ORDER BY c.displayOrder ASC, c.name ASC")
    List<MonthlyBalance> findByUserAndYearMonth(
            @Param("user") AppUser user,
            @Param("yearMonth") LocalDate yearMonth);
}
