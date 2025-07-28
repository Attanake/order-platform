package arch.attanake.repository;

import arch.attanake.entity.NotificationTemplateEntity;
import arch.attanake.entity.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplateEntity, String> {
    Optional<NotificationTemplateEntity> findByTypeAndLanguageCode(NotificationType type, String languageCode);
    List<NotificationTemplateEntity> findByType(NotificationType type);
    Optional<NotificationTemplateEntity> findByTemplateIdAndVersion(String templateId, int version);
}
