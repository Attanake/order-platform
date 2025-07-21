package arch.attanake.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
}
