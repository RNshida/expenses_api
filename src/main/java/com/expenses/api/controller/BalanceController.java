package com.expenses.api.controller;

import com.expenses.api.dto.BalanceInputItemDto;
import com.expenses.api.dto.BalanceSaveRequest;
import com.expenses.api.dto.BalanceSummaryResponse;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;
    private final AppUserRepository userRepository;

    @GetMapping("/summary")
    public BalanceSummaryResponse getSummary(Principal principal,
            @RequestParam(defaultValue = "12") int months) {
        return balanceService.getSummary(currentUser(principal), months);
    }

    @GetMapping("/input")
    public List<BalanceInputItemDto> getInputBalances(Principal principal,
            @RequestParam String yearMonth) {
        return balanceService.getInputBalances(currentUser(principal), yearMonth);
    }

    @PostMapping("/input")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveBalances(Principal principal, @RequestBody @Valid BalanceSaveRequest request) {
        balanceService.saveBalances(currentUser(principal), request);
    }

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが見つかりません"));
    }
}
