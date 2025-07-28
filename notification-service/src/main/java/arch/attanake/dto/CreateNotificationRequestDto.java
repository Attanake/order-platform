package arch.attanake.dto;

import arch.attanake.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateNotificationRequestDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String orderId;

    @NotNull
    private NotificationType type;

    private String message;
}
