package arch.attanake.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private Boolean active;
}
