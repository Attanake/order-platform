package arch.attanake.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "templates")
@Data
public class NotificationTemplateEntity {
    @Id
    private String id;
    private String templateId;
    private String subject;
    private String content;
    private NotificationType type;
    private String languageCode = "en";
    private int version = 1;
}
