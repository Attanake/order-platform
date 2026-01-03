package arch.attanake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCredentialsDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
