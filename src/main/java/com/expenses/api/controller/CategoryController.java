package com.expenses.api.controller;

import com.expenses.api.dto.CategoryDto;
import com.expenses.api.dto.CategoryReorderRequest;
import com.expenses.api.dto.CategoryRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AppUserRepository userRepository;

    @GetMapping
    public List<CategoryDto> getAll(Principal principal) {
        return categoryService.getAll(currentUser(principal));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(Principal principal, @RequestBody @Valid CategoryRequest request) {
        return categoryService.add(currentUser(principal), request);
    }

    @PutMapping("/{id}")
    public CategoryDto update(Principal principal, @PathVariable Long id,
                              @RequestBody @Valid CategoryRequest request) {
        return categoryService.update(currentUser(principal), id, request);
    }

    @PutMapping("/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorder(Principal principal, @RequestBody CategoryReorderRequest request) {
        categoryService.reorder(currentUser(principal), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal, @PathVariable Long id) {
        categoryService.delete(currentUser(principal), id);
    }

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが見つかりません"));
    }
}
