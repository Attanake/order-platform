package arch.attanake.controller;


import arch.attanake.service.UserService;
import arch.attanake.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public String createUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable("id") String id) throws ChangeSetPersister.NotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) throws ChangeSetPersister.NotFoundException {
        return userService.getUserByUsername(username);
    }
}
