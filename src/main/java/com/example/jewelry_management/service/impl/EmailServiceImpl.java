package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.config.FrontendProperties;
import com.example.jewelry_management.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final FrontendProperties frontendProperties;

    public void sendOtpEmailHtml(String to, String otp) {
        try {
            String resetLink = frontendProperties.getResetPasswordUrl() + "?otp=" + otp;

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("resetLink", resetLink);

            String htmlContent = templateEngine.process("otp-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Mã xác thực OTP của bạn");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email OTP thất bại", e);
        }
    }

    @Override
    public void sendPasswordChangedConfirmationHtml(String to) {
        try {
            Context context = new Context();
            context.setVariable("email", to);

            String htmlContent = templateEngine.process("password-changed-email", context);

            // 2. Create MIME email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Xác nhận thay đổi mật khẩu");
            helper.setText(htmlContent, true);

            // 3. Send email
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }
}

