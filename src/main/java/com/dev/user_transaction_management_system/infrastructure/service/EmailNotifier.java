package com.dev.user_transaction_management_system.infrastructure.service;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.user.Email;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!test")
public class EmailNotifier implements Notifier {

    public static final String PREFIX_SUBJECT = "Notification! ";
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;


    @Override
    public void sendSimpleMessage(Subject subject, Message message, Email to) {
        Assert.notNull(message, "message cannot be null");
        Assert.notNull(to, "receiver email cannot be null");

        try {
            final var simpleMailMessage = createSimpleMessage(subject ,message, to);
            mailSender.send(simpleMailMessage);

            log(to.asString(), simpleMailMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("email cant sent");
        }
    }

    private static void log(String to, SimpleMailMessage message) {
        final String emailBody = format("##Sending Email... : %s to %s", message, to);
        log.info(emailBody);
    }

    private SimpleMailMessage createSimpleMessage(Subject subject , Message message, Email to) {
        final var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(PREFIX_SUBJECT + subject.subject());
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(to.asString());
        simpleMailMessage.setText(message.body());
        return simpleMailMessage;
    }
}
