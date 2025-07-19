package arch.attanake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaResponse {
    private UUID id;
    private String subject;
    private String format;
    private String schemaContent;
    private int version;
    private boolean active;
}