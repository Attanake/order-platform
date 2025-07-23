package arch.attanake.kafka.producer;

import arch.attanake.entity.ProductEntity;
import arch.attanake.product.kafka.EventType;
import arch.attanake.product.kafka.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProductCreatedEvent(ProductEntity product) {
        ProductEvent event = ProductEvent.newBuilder()
                .setProductId(product.getId())
                .setName(product.getName())
                .setCategory(product.getCategory())
                .setPrice(product.getPrice().toString())
                .setStock(product.getStock())
                .setEventType(EventType.CREATED)
                .setTimestamp(System.currentTimeMillis())
                .build();

        kafkaTemplate.send("product.events", product.getId(), event);
    }
}