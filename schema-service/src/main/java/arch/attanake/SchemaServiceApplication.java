package arch.attanake;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SchemaServiceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .filename("secrets.env")
                .load();
        System.setProperty("SCHEMA_DB_PASSWORD", dotenv.get("password"));
        new SpringApplicationBuilder(SchemaServiceApplication.class).run(args);
    }
}
