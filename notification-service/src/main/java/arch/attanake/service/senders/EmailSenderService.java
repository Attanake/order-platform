package arch.attanake.service.senders;

import arch.attanake.exception.NotificationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void init() {
        if (mailSender == null) {
            log.error("JavaMailSender is not configured!");
            throw new IllegalStateException("JavaMailSender is not configured");
        }
        log.info("Email sender service initialized successfully");
    }

    public void sendEmail(String to, String subject, String text) {
        if (!StringUtils.hasText(to) || !StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("Email 'to' and 'subject' must not be empty");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent to: {}, subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new NotificationException("Failed to send email to " + to, e);
        }
    }
}
