package com.example.datacentermonitoringapi.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendRegistrationEmail(String username, String password, String email) {
        sendSimpleEmail(
                email,
                "Registration success",
                """
                 Поздравляем с успешной регистрацией в системе, ваши данные для входа:
                 
                 Форма для входа: https://kryukov.cloud/login
                 Имя пользователя: %s
                 Пароль: %s
                 """.formatted(username, password)
        );
    }
}
