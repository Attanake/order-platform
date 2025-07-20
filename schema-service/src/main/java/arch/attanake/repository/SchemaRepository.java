package arch.attanake.repository;

import arch.attanake.entity.SchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchemaRepository extends JpaRepository<SchemaEntity, UUID> {

    Optional<SchemaEntity> findBySubjectAndVersion(String subject, int version);

    List<SchemaEntity> findBySubjectOrderByVersionDesc(String subject);

    boolean existsBySubjectAndSchemaContent(String subject, String schemaContent);
}
