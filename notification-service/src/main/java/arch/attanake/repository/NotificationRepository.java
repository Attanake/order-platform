package arch.attanake.repository;

import arch.attanake.entity.NotificationEntity;
import arch.attanake.entity.NotificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    List<NotificationEntity> findByUserId(String userId);
    List<NotificationEntity> findByStatus(NotificationStatus status);
    List<NotificationEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<NotificationEntity> findByOrderId(String orderId);
}
