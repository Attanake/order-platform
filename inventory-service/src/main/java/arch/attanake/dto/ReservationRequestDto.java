package arch.attanake.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private String orderId;
    private List<InventoryItemDto> items;

    public static ReservationRequestDto fromMap(String orderId, Map<String, Integer> itemsMap) {
        List<InventoryItemDto> items = itemsMap.entrySet().stream()
                .map(entry -> InventoryItemDto.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return ReservationRequestDto.builder()
                .orderId(orderId)
                .items(items)
                .build();
    }
}
