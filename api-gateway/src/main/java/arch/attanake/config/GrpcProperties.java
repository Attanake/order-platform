package arch.attanake.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "grpc")
public class GrpcProperties {
    private int port = 9099;
    private boolean enabled = true;
}
