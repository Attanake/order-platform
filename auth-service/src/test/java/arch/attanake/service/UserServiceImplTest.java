package arch.attanake.service;

import arch.attanake.dto.*;
import arch.attanake.entity.UserEntity;
import arch.attanake.mapper.UserMapper;
import arch.attanake.repository.UserRepository;
import arch.attanake.security.jwt.JwtService;
import arch.attanake.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_ShouldEncodePasswordAndSaveUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("rawPassword");
        userDto.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        when(userMapper.toEntity(userDto)).thenReturn(userEntity);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        String result = userService.addUser(userDto);

        assertEquals("User added", result);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(userEntity);
        assertEquals("encodedPassword", userEntity.getPassword());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId.toString());

        assertEquals("testUser", result.getUsername());
    }

    @Test
    void signIn_ShouldReturnTokens_WhenCredentialsValid() throws Exception {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername("testUser");
        credentials.setPassword("correctPass");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPass");

        JwtAuthenticationDto tokens = new JwtAuthenticationDto();
        tokens.setToken("accessToken");
        tokens.setRefreshToken("refreshToken");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("correctPass", "encodedPass")).thenReturn(true);
        when(jwtService.generateAuthToken("testUser")).thenReturn(tokens);

        JwtAuthenticationDto result = userService.signIn(credentials);

        assertEquals("accessToken", result.getToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    void refreshToken_ShouldReturnNewTokens_WhenTokenValid() throws Exception {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken("valid.refresh.token");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        JwtAuthenticationDto newTokens = new JwtAuthenticationDto();
        newTokens.setToken("newAccess");
        newTokens.setRefreshToken("newRefresh");

        when(jwtService.validateJwtToken("valid.refresh.token")).thenReturn(true);
        when(jwtService.getUsernameFromToken("valid.refresh.token")).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(jwtService.refreshBaseToken("testUser", "valid.refresh.token")).thenReturn(newTokens);

        JwtAuthenticationDto result = userService.refreshToken(refreshTokenDto);

        assertEquals("newAccess", result.getToken());
    }

    @Test
    void signIn_ShouldThrow_WhenCredentialsInvalid() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername("testUser");
        credentials.setPassword("wrongPass");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPass");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> {
            userService.signIn(credentials);
        });
    }

    @Test
    void getUserById_ShouldThrow_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> {
            userService.getUserById(userId.toString());
        });
    }
}