package com.expenses.api.repository;

import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.FurusatoConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FurusatoConfigRepository extends JpaRepository<FurusatoConfig, Long> {

    Optional<FurusatoConfig> findByUserAndFiscalYear(AppUser user, Integer fiscalYear);
}
