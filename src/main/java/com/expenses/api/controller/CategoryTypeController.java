package com.expenses.api.controller;

import com.expenses.api.dto.CategoryTypeDto;
import com.expenses.api.dto.CategoryTypeReorderRequest;
import com.expenses.api.dto.CategoryTypeRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.service.CategoryTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/category-types")
@RequiredArgsConstructor
public class CategoryTypeController {

    private final CategoryTypeService categoryTypeService;
    private final AppUserRepository userRepository;

    @GetMapping
    public List<CategoryTypeDto> getAll(Principal principal) {
        return categoryTypeService.getAll(currentUser(principal));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryTypeDto add(Principal principal, @RequestBody @Valid CategoryTypeRequest request) {
        return categoryTypeService.add(currentUser(principal), request);
    }

    @PutMapping("/{id}")
    public CategoryTypeDto update(Principal principal, @PathVariable Long id,
                                  @RequestBody @Valid CategoryTypeRequest request) {
        return categoryTypeService.update(currentUser(principal), id, request);
    }

    @PutMapping("/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorder(Principal principal, @RequestBody CategoryTypeReorderRequest request) {
        categoryTypeService.reorder(currentUser(principal), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal, @PathVariable Long id) {
        categoryTypeService.delete(currentUser(principal), id);
    }

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが見つかりません"));
    }
}
