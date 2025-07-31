package arch.attanake;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProductServiceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .filename("secrets.env")
                .load();
        System.setProperty("PRODUCT_DB_PASSWORD", dotenv.get("mongoPassword"));
        new SpringApplicationBuilder(ProductServiceApplication.class).run(args);
    }
}
