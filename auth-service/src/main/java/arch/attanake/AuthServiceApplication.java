package arch.attanake;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServiceApplication.class).run(args);

    }
}
