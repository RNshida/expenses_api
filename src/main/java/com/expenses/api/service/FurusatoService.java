package com.expenses.api.service;

import com.expenses.api.dto.FurusatoConfigDto;
import com.expenses.api.dto.FurusatoConfigSaveRequest;
import com.expenses.api.dto.FurusatoContributionDto;
import com.expenses.api.dto.FurusatoEntrySaveRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.FurusatoConfig;
import com.expenses.api.entity.FurusatoContribution;
import com.expenses.api.repository.FurusatoConfigRepository;
import com.expenses.api.repository.FurusatoContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FurusatoService {

    private final FurusatoContributionRepository contributionRepository;
    private final FurusatoConfigRepository configRepository;

    @Transactional(readOnly = true)
    public List<FurusatoContributionDto> getEntries(AppUser user, int fiscalYear) {
        return contributionRepository
                .findByUserAndFiscalYearOrderByCreatedAtAsc(user, fiscalYear)
                .stream()
                .map(FurusatoContributionDto::from)
                .toList();
    }

    @Transactional
    public FurusatoContributionDto addEntry(AppUser user, int fiscalYear, FurusatoEntrySaveRequest req) {
        FurusatoContribution c = new FurusatoContribution(
                user, fiscalYear,
                req.getMunicipality().trim(),
                req.getProductName() != null ? req.getProductName().trim() : null,
                req.getAmount(),
                req.getStatus()
        );
        return FurusatoContributionDto.from(contributionRepository.save(c));
    }

    @Transactional
    public FurusatoContributionDto updateEntry(AppUser user, Long id, FurusatoEntrySaveRequest req) {
        FurusatoContribution c = contributionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "寄付データが見つかりません"));
        c.setMunicipality(req.getMunicipality().trim());
        c.setProductName(req.getProductName() != null ? req.getProductName().trim() : null);
        c.setAmount(req.getAmount());
        c.setStatus(req.getStatus());
        return FurusatoContributionDto.from(contributionRepository.save(c));
    }

    @Transactional
    public void deleteEntry(AppUser user, Long id) {
        FurusatoContribution c = contributionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "寄付データが見つかりません"));
        contributionRepository.delete(c);
    }

    @Transactional(readOnly = true)
    public FurusatoConfigDto getConfig(AppUser user, int fiscalYear) {
        return configRepository.findByUserAndFiscalYear(user, fiscalYear)
                .map(FurusatoConfigDto::from)
                .orElse(FurusatoConfigDto.empty());
    }

    @Transactional
    public void saveConfig(AppUser user, int fiscalYear, FurusatoConfigSaveRequest req) {
        FurusatoConfig config = configRepository.findByUserAndFiscalYear(user, fiscalYear)
                .orElse(new FurusatoConfig(user, fiscalYear));
        config.setAnnualIncome(req.getAnnualIncome());
        config.setLimitAmount(req.getLimitAmount());
        configRepository.save(config);
    }
}
