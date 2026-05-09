package com.expenses.api.controller;

import com.expenses.api.dto.AuthRequest;
import com.expenses.api.dto.AuthResponse;
import com.expenses.api.dto.ForgotPasswordRequest;
import com.expenses.api.dto.ResetPasswordRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.PasswordResetToken;
import com.expenses.api.repository.AppUserRepository;
import com.expenses.api.repository.PasswordResetTokenRepository;
import com.expenses.api.security.JwtUtil;
import com.expenses.api.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("このメールアドレスはすでに登録されています");
        }
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, user.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(new AuthResponse(token, user.getEmail()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .<AuthResponse>build());
    }

    @PostMapping("/forgot-password")
    @Transactional
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        // メールが存在しない場合も同じレスポンスを返す（ユーザー列挙攻撃対策）
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            resetTokenRepository.deleteByEmail(request.getEmail());
            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusHours(1);
            resetTokenRepository.save(new PasswordResetToken(token, request.getEmail(), expiry));
            try {
                emailService.sendPasswordResetEmail(request.getEmail(), token);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("メールの送信に失敗しました。しばらく後に再試行してください。");
            }
        }
        return ResponseEntity.ok("パスワード再設定メールを送信しました（登録済みの場合）");
    }

    @PostMapping("/reset-password")
    @Transactional
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElse(null);

        if (resetToken == null || resetToken.isUsed()) {
            return ResponseEntity.badRequest().body("無効なトークンです");
        }
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            return ResponseEntity.badRequest().body("トークンの有効期限が切れています。再度パスワード再設定を申請してください。");
        }

        AppUser user = userRepository.findByEmail(resetToken.getEmail())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("ユーザーが見つかりません");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);

        return ResponseEntity.ok("パスワードを再設定しました");
    }
}
