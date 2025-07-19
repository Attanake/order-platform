package arch.attanake.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "schemas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String subject;

    @Column(nullable = false)
    private String format;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String schemaContent;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private boolean isActive = true;
}
