package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.Notifier;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!test")
public class EmailNotifierWithGmail implements Notifier {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;


    @Override
    public void send(NotifiableEvent event) {
        Assert.notNull(event,"event cannot be null");

        final String emailBody = String.format("##Sending Email... : %s to %s", event.getMessage(), event.toEmail());
        try {
            final SimpleMailMessage message = createSimpleMessage(event);
            mailSender.send(message);

            log.info(emailBody);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new IllegalArgumentException("email cant sent");
        }
    }

    private SimpleMailMessage createSimpleMessage(NotifiableEvent event) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Notification! " + event.getSubject());
        message.setFrom(fromEmail);
        message.setTo(event.toEmail());
        message.setText(event.getMessage());
        return message;
    }
}
