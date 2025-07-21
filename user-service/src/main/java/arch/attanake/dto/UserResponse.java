package arch.attanake.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private Set<String> roles;
}
