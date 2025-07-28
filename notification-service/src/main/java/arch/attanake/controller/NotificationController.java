package arch.attanake.controller;

import arch.attanake.dto.CreateNotificationRequestDto;
import arch.attanake.dto.NotificationDto;
import arch.attanake.dto.NotificationResponseDto;
import arch.attanake.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(
            @RequestBody CreateNotificationRequestDto requestDto) {
        return ResponseEntity.ok(notificationService.createNotification(requestDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getUserNotifications(
            @PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotification(
            @PathVariable String id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }
}
