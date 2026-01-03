package arch.attanake.controller;


import arch.attanake.service.UserService;
import arch.attanake.dto.JwtAuthenticationDto;
import arch.attanake.dto.RefreshTokenDto;
import arch.attanake.dto.UserCredentialsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationDto> signIn(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {

        try{
            JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        }catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
