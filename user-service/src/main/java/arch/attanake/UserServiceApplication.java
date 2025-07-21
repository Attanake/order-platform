package arch.attanake;

import arch.attanake.mapper.UserMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        var DI = new SpringApplicationBuilder(UserServiceApplication.class).run(args);
    }
}
