package arch.attanake;

import arch.attanake.security.jwt.JwtProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class AuthServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServiceApplication.class).run(args);
    }
}
