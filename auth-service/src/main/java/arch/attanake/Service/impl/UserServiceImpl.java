package arch.attanake.Service.impl;
import arch.attanake.Service.UserService;
import arch.attanake.dto.JwtAuthenticationDto;
import arch.attanake.dto.RefreshTokenDto;
import arch.attanake.dto.UserCredentialsDto;
import arch.attanake.dto.UserDto;
import arch.attanake.entity.UserEntity;
import arch.attanake.mapper.UserMapper;
import arch.attanake.repository.UserRepository;
import arch.attanake.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        UserEntity user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user.getUsername());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            UserEntity user = findByUsername(jwtService.getUsernameFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getUsername(), refreshToken);
        }
        throw new  AuthenticationException("Invalid refresh token");
    }

    @Override
    @Transactional
    public UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException {
        return userMapper.toDto(userRepository.findByUserId(UUID.fromString(id))
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    @Transactional
    public UserDto getUserByUsername(String username) throws ChangeSetPersister.NotFoundException {
        return userMapper.toDto(userRepository.findByUsername(username)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    public String addUser(UserDto userDto){
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User added";
    }

    private UserEntity findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userCredentialsDto.getUsername());
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Username or password is not correct");
    }

    private UserEntity findByUsername(String username) throws Exception {
        return userRepository.findByUsername(username).orElseThrow(()->
                new Exception(String.format("User with username %s not found", username)));
    }
}