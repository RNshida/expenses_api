package com.expenses.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("【カケログマップ】パスワード再設定のご案内");
        message.setText(
            "パスワード再設定のリクエストを受け付けました。\n\n" +
            "以下のURLをクリックしてパスワードを再設定してください。\n" +
            "このURLは1時間有効です。\n\n" +
            resetUrl + "\n\n" +
            "このメールに心当たりがない場合は、無視してください。\n" +
            "パスワードは変更されません。\n\n" +
            "カケログマップ"
        );
        log.info("メール送信開始: to={}, from={}", toEmail, fromAddress);
        mailSender.send(message);
        log.info("メール送信完了: to={}", toEmail);
    }
}
