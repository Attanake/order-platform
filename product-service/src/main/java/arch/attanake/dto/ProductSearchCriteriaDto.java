package arch.attanake.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

@Data
public class ProductSearchCriteriaDto {
    private String name;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Boolean isPublished;
    private Pageable pageable;
}