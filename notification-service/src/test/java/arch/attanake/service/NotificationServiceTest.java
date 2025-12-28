package arch.attanake.service;

import arch.attanake.dto.CreateNotificationRequestDto;
import arch.attanake.dto.NotificationResponseDto;
import arch.attanake.entity.NotificationEntity;
import arch.attanake.entity.NotificationStatus;
import arch.attanake.entity.NotificationTemplateEntity;
import arch.attanake.entity.NotificationType;
import arch.attanake.mapper.NotificationMapper;
import arch.attanake.repository.NotificationRepository;
import arch.attanake.repository.NotificationTemplateRepository;
import arch.attanake.service.senders.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotification_ShouldSaveNotification() {
        CreateNotificationRequestDto request = new CreateNotificationRequestDto();
        request.setUserId("user1");
        request.setOrderId("order1");
        request.setType(NotificationType.EMAIL);

        NotificationEntity savedEntity = new NotificationEntity();
        NotificationResponseDto expectedResponse = new NotificationResponseDto();

        when(notificationRepository.save(any())).thenReturn(savedEntity);
        when(notificationMapper.toResponseDto(savedEntity)).thenReturn(expectedResponse);

        NotificationResponseDto response = notificationService.createNotification(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(notificationRepository).save(any(NotificationEntity.class));
    }

    @Test
    void processPendingNotifications_ShouldSendEmail() {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationType.EMAIL);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setUserId("user@example.com");
        notification.setOrderId("563464");

        NotificationTemplateEntity template = new NotificationTemplateEntity();
        template.setContent("Test template {orderId}");
        template.setType(NotificationType.EMAIL);
        template.setLanguageCode("en");

        when(notificationRepository.findByStatus(NotificationStatus.PENDING))
                .thenReturn(List.of(notification));
        when(templateRepository.findByTypeAndLanguageCode(any(), any()))
                .thenReturn(Optional.of(template));

        notificationService.processPendingNotifications();

        verify(emailSenderService).sendEmail(any(), any(), any());
        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }
}
