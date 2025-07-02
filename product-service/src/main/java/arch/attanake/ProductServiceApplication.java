package arch.attanake;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProductServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductServiceApplication.class).run(args);
    }
}
