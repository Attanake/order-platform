package arch.attanake.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponseDto {
    private String productId;
    private int total;
    private int reserved;
    private int available;
}
