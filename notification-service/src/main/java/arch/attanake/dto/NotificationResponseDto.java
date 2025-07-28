package arch.attanake.dto;

import arch.attanake.entity.NotificationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDto {
    private String id;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private String details;
}
