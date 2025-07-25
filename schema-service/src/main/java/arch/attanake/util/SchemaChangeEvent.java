package arch.attanake.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaChangeEvent {
    private String subject;
    private UUID schemaId;
}
