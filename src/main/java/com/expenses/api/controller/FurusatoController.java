package com.expenses.api.controller;

import com.expenses.api.dto.FurusatoConfigDto;
import com.expenses.api.dto.FurusatoConfigSaveRequest;
import com.expenses.api.dto.FurusatoContributionDto;
import com.expenses.api.dto.FurusatoEntrySaveRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.service.FurusatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/furusato")
@RequiredArgsConstructor
public class FurusatoController {

    private final FurusatoService furusatoService;
    private final AppUserRepository userRepository;

    @GetMapping("/{fiscalYear}/entries")
    public List<FurusatoContributionDto> getEntries(Principal principal, @PathVariable int fiscalYear) {
        return furusatoService.getEntries(currentUser(principal), fiscalYear);
    }

    @PostMapping("/{fiscalYear}/entries")
    @ResponseStatus(HttpStatus.CREATED)
    public FurusatoContributionDto addEntry(Principal principal,
                                            @PathVariable int fiscalYear,
                                            @RequestBody @Valid FurusatoEntrySaveRequest request) {
        return furusatoService.addEntry(currentUser(principal), fiscalYear, request);
    }

    @PutMapping("/entries/{id}")
    public FurusatoContributionDto updateEntry(Principal principal,
                                               @PathVariable Long id,
                                               @RequestBody @Valid FurusatoEntrySaveRequest request) {
        return furusatoService.updateEntry(currentUser(principal), id, request);
    }

    @DeleteMapping("/entries/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(Principal principal, @PathVariable Long id) {
        furusatoService.deleteEntry(currentUser(principal), id);
    }

    @GetMapping("/{fiscalYear}/config")
    public FurusatoConfigDto getConfig(Principal principal, @PathVariable int fiscalYear) {
        return furusatoService.getConfig(currentUser(principal), fiscalYear);
    }

    @PutMapping("/{fiscalYear}/config")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveConfig(Principal principal,
                           @PathVariable int fiscalYear,
                           @RequestBody FurusatoConfigSaveRequest request) {
        furusatoService.saveConfig(currentUser(principal), fiscalYear, request);
    }

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが見つかりません"));
    }
}
