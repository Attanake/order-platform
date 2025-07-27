package arch.attanake.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequestDto {
    @NotBlank
    private String productId;

    @Positive
    private int quantity;
}
