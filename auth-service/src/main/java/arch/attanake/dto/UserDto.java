package arch.attanake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String username;
    @NotBlank(message = "Password cannot be blank")
    String password;
    String email;
}