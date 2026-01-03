package arch.attanake;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(InventoryServiceApplication.class).run(args);
    }
}
