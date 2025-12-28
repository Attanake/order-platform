package arch.attanake.controller;

import arch.attanake.service.UserService;
import arch.attanake.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto testUserDto;
    private final String testId = "123e4567-e89b-12d3-a456-426614174000";
    private final String testUsername = "testUser";

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto();
        testUserDto.setUsername(testUsername);
        testUserDto.setPassword("password");
    }

    @Test
    void createUser_ShouldReturnSuccessMessage() {
        when(userService.addUser(testUserDto)).thenReturn("User added");

        String result = userController.createUser(testUserDto);

        assertEquals("User added", result);
        verify(userService, times(1)).addUser(testUserDto);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws ChangeSetPersister.NotFoundException {
        when(userService.getUserById(testId)).thenReturn(testUserDto);

        UserDto result = userController.getUserById(testId);

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
        verify(userService, times(1)).getUserById(testId);
    }

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserNotExists() throws ChangeSetPersister.NotFoundException {
        when(userService.getUserById(testId)).thenThrow(ChangeSetPersister.NotFoundException.class);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> {
            userController.getUserById(testId);
        });
        verify(userService, times(1)).getUserById(testId);
    }

    @Test
    void getUserByUsername_ShouldReturnUser_WhenUserExists() throws ChangeSetPersister.NotFoundException {
        when(userService.getUserByUsername(testUsername)).thenReturn(testUserDto);

        UserDto result = userController.getUserByUsername(testUsername);

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
        verify(userService, times(1)).getUserByUsername(testUsername);
    }

    @Test
    void getUserByUsername_ShouldThrowNotFoundException_WhenUserNotExists() throws ChangeSetPersister.NotFoundException {
        when(userService.getUserByUsername(testUsername)).thenThrow(ChangeSetPersister.NotFoundException.class);
        assertThrows(ChangeSetPersister.NotFoundException.class, () -> {
            userController.getUserByUsername(testUsername);
        });
        verify(userService, times(1)).getUserByUsername(testUsername);
    }
}