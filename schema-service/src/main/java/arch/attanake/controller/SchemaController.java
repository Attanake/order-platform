package arch.attanake.controller;

import arch.attanake.dto.SchemaId;
import arch.attanake.dto.SchemaRequest;
import arch.attanake.dto.SchemaResponse;
import arch.attanake.entity.SchemaEntity;
import arch.attanake.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schemas")
@RequiredArgsConstructor
public class SchemaController {

    private final SchemaService schemaService;

    @PostMapping("/{subject}")
    public ResponseEntity<SchemaId> registerSchema(
            @PathVariable String subject,
            @RequestBody SchemaRequest request
    ) {
        SchemaId schemaId = schemaService.registerSchema(subject, request.getSchema());
        return ResponseEntity.ok(schemaId);
    }

    @GetMapping("/{subject}/versions/{version}")
    public ResponseEntity<SchemaResponse> getSchema(
            @PathVariable String subject,
            @PathVariable int version
    ) throws ChangeSetPersister.NotFoundException {
        SchemaEntity schema = schemaService.getSchema(subject, version);
        SchemaResponse response = new SchemaResponse(
                schema.getId(),
                schema.getSubject(),
                schema.getFormat(),
                schema.getSchemaContent(),
                schema.getVersion(),
                schema.isActive()
        );
        return ResponseEntity.ok(response);
    }
}