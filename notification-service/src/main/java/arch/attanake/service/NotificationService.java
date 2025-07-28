package arch.attanake.service;

import arch.attanake.dto.CreateNotificationRequestDto;
import arch.attanake.dto.NotificationDto;
import arch.attanake.dto.NotificationResponseDto;
import arch.attanake.entity.NotificationEntity;
import arch.attanake.entity.NotificationStatus;
import arch.attanake.entity.NotificationTemplateEntity;
import arch.attanake.entity.NotificationType;
import arch.attanake.exception.NotificationException;
import arch.attanake.mapper.NotificationMapper;
import arch.attanake.repository.NotificationRepository;
import arch.attanake.repository.NotificationTemplateRepository;
import arch.attanake.service.senders.EmailSenderService;
import arch.attanake.service.senders.PushNotificationService;
import arch.attanake.service.senders.SmsSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final EmailSenderService emailSenderService;
    private final SmsSenderService smsSenderService;
    private final PushNotificationService pushNotificationService;
    private final NotificationMapper notificationMapper;

    @Transactional
    public NotificationResponseDto createNotification(CreateNotificationRequestDto requestDto) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(requestDto.getUserId());
        notification.setOrderId(requestDto.getOrderId());
        notification.setType(requestDto.getType());
        notification.setMessage(requestDto.getMessage());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        NotificationEntity savedNotification = notificationRepository.save(notification);

        return notificationMapper.toResponseDto(savedNotification);
    }

    @Transactional
    public void processPendingNotifications() {
        List<NotificationEntity> pendingNotifications = notificationRepository.findByStatus(NotificationStatus.PENDING);

        pendingNotifications.forEach(this::processSingleNotification);
    }

    private void processSingleNotification(NotificationEntity notification) {
        try {
            notification.setStatus(NotificationStatus.PROCESSING);
            notificationRepository.save(notification);

            Optional<NotificationTemplateEntity> templateOpt = templateRepository
                    .findByTypeAndLanguageCode(notification.getType(), "en"); // или язык пользователя

            if (templateOpt.isPresent()) {
                NotificationTemplateEntity template = templateOpt.get();
                String formattedMessage = formatMessage(template.getContent(), notification);

                sendNotificationByType(notification, template.getSubject(), formattedMessage);

                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
            } else {
                throw new NotificationException("Template not found for type: " + notification.getType());
            }
        } catch (Exception e) {
            handleNotificationFailure(notification, e);
        } finally {
            notificationRepository.save(notification);
        }
    }

    private String formatMessage(String template, NotificationEntity notification) {
        return template
                .replace("{orderId}", notification.getOrderId())
                .replace("{userId}", notification.getUserId())
                .replace("{message}", notification.getMessage() != null ? notification.getMessage() : "");
    }

    private void sendNotificationByType(NotificationEntity notification, String subject, String message) {
        switch (notification.getType()) {
            case EMAIL:
                emailSenderService.sendEmail(notification.getUserId(), subject, message);
                break;
            case SMS:
                smsSenderService.sendSms(notification.getUserId(), message);
                break;
            case PUSH:
                pushNotificationService.sendPush(notification.getUserId(), subject, message);
                break;
            default:
                throw new NotificationException("Unsupported notification type: " + notification.getType());
        }
    }

    private void handleNotificationFailure(NotificationEntity notification, Exception e) {
        log.error("Failed to process notification: {}", notification.getId(), e);
        notification.setStatus(NotificationStatus.FAILED);
        notification.setErrorMessage(e.getMessage());
        notification.setRetryCount(notification.getRetryCount() + 1);
    }

    public List<NotificationDto> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    public NotificationDto getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new NotificationException("Notification not found with id: " + id));
    }

    @Transactional
    public void retryFailedNotifications() {
        List<NotificationEntity> failedNotifications = notificationRepository.findByStatus(NotificationStatus.FAILED);
        failedNotifications.forEach(this::processSingleNotification);
    }
}