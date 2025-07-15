package arch.attanake.Service;

import arch.attanake.dto.RefreshTokenDto;
import arch.attanake.dto.UserCredentialsDto;
import arch.attanake.dto.UserDto;
import arch.attanake.dto.JwtAuthenticationDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import javax.naming.AuthenticationException;

public interface UserService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
    UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException;
    UserDto getUserByUsername(String username) throws ChangeSetPersister.NotFoundException;
    String addUser(UserDto user);
}
