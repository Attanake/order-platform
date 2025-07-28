package arch.attanake.service.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushNotificationService {

    public void sendPush(String deviceToken, String title, String body) {
        // В реальной реализации здесь, по идее, должна быть интеграция с FCM и APNS
        log.info("Push notification sent to device: {}, title: {}, body: {}", deviceToken, title, body);
    }
}
