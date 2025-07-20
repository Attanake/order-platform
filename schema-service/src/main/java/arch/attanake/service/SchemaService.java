package arch.attanake.service;

import arch.attanake.dto.SchemaId;
import arch.attanake.entity.SchemaEntity;
import arch.attanake.exception.IncompatibleSchemaException;
import arch.attanake.exception.SchemaAlreadyExistsException;
import arch.attanake.repository.SchemaRepository;
import arch.attanake.util.AvroCompatibilityChecker;
import arch.attanake.util.SchemaChangeEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchemaService {

    private final SchemaRepository schemaRepository;
    private final KafkaTemplate<String, SchemaChangeEvent> kafkaTemplate;

    @Transactional
    public SchemaId registerSchema(String subject, String schemaJson) {
        if (schemaRepository.existsBySubjectAndSchemaContent(subject, schemaJson)) {
            throw new SchemaAlreadyExistsException();
        }

        if (!isSchemaCompatible(subject, schemaJson)) {
            throw new IncompatibleSchemaException();
        }

        int nextVersion = getNextVersion(subject);

        SchemaEntity schema = new SchemaEntity();
        schema.setSubject(subject);
        schema.setSchemaContent(schemaJson);
        schema.setFormat("AVRO");
        schema.setVersion(nextVersion);
        schema.setActive(true);
        schemaRepository.save(schema);

        kafkaTemplate.send(
                "schema.changes",
                new SchemaChangeEvent(subject, schema.getId())
        );

        return new SchemaId(schema.getId());
    }

    private int getNextVersion(String subject) {
        return schemaRepository.findBySubjectOrderByVersionDesc(subject)
                .stream()
                .findFirst()
                .map(SchemaEntity::getVersion)
                .orElse(0) + 1;
    }

    private boolean isSchemaCompatible(String subject, String newSchema) {
        Optional<SchemaEntity> lastSchema = schemaRepository.findBySubjectOrderByVersionDesc(subject)
                .stream()
                .findFirst();

        if (lastSchema.isEmpty()) {
            return true;
        }

        try {
            Schema.Parser parser = new Schema.Parser();
            Schema oldAvroSchema = parser.parse(lastSchema.get().getSchemaContent());
            Schema newAvroSchema = parser.parse(newSchema);
            return AvroCompatibilityChecker.checkCompatibility(oldAvroSchema, newAvroSchema);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse schemas", e);
        }
    }

    public SchemaEntity getSchema(String subject, int version)
            throws ChangeSetPersister.NotFoundException {
        return schemaRepository.findBySubjectAndVersion(subject, version)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}