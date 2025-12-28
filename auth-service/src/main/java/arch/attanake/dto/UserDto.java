package arch.attanake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    String firstName;
    String lastName;
    String username;
    @NotBlank(message = "Password cannot be blank")
    String password;
    String email;
}