package arch.attanake.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class ProductResponseDto {
    private String id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;
}