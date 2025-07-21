package arch.attanake.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRolesUpdateRequest {
    private Set<String> roles;
}
