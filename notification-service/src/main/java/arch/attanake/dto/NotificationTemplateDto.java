package arch.attanake.dto;

import arch.attanake.entity.NotificationType;
import lombok.Data;

@Data
public class NotificationTemplateDto {
    private String id;
    private String templateId;
    private String subject;
    private String content;
    private NotificationType type;
    private String languageCode;
    private int version;
}
