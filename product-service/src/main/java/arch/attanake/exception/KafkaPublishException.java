package arch.attanake.exception;

public class KafkaPublishException extends RuntimeException {
    public KafkaPublishException(String topic, String key, Throwable cause) {
        super(String.format(
                "Failed to publish message to Kafka topic '%s' with key '%s'",
                topic, key
        ), cause);
    }
}
