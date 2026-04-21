package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.FurusatoContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FurusatoContributionRepository extends JpaRepository<FurusatoContribution, Long> {

    List<FurusatoContribution> findByUserAndFiscalYearOrderByCreatedAtAsc(AppUser user, Integer fiscalYear);

    Optional<FurusatoContribution> findByIdAndUser(Long id, AppUser user);
}
