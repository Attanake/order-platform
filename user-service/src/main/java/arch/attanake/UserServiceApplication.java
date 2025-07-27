package arch.attanake;

import arch.attanake.mapper.UserMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .filename("secrets.env")
                .load();
        System.setProperty("password", dotenv.get("password"));
        var DI = new SpringApplicationBuilder(UserServiceApplication.class).run(args);
    }
}
