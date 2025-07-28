package arch.attanake.service.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsSenderService {

    public void sendSms(String phoneNumber, String message) {
        // В реальной реализации здесь должна быть реализация с смс-шлюзом)))
        log.info("SMS sent to: {}, message: {}", phoneNumber, message);
    }
}