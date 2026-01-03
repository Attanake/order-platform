package arch.attanake;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SchemaServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SchemaServiceApplication.class).run(args);
    }
}
