package arch.attanake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtAuthenticationDto {
    @NotBlank
    private String token;
    @NotBlank
    private String refreshToken;
}
