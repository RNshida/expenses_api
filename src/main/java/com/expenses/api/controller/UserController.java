package com.expenses.api.controller;

import com.expenses.api.dto.AuthResponse;
import com.expenses.api.dto.UpdateEmailRequest;
import com.expenses.api.dto.UpdatePasswordRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private AppUser currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/me")
    public AuthResponse getMe(Principal principal) {
        AppUser user = currentUser(principal);
        return new AuthResponse(null, user.getEmail());
    }

    @PutMapping("/email")
    public AuthResponse updateEmail(@Valid @RequestBody UpdateEmailRequest request, Principal principal) {
        AppUser user = currentUser(principal);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "現在のパスワードが正しくありません");
        }
        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "このメールアドレスはすでに使用されています");
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);

        String newToken = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(newToken, user.getEmail());
    }

    @PutMapping("/password")
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request, Principal principal) {
        AppUser user = currentUser(principal);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "現在のパスワードが正しくありません");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
