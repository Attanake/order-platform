package arch.attanake.dto;

import java.util.Set;
import java.util.UUID;

public record UserRolesUpdatedEvent(
        UUID userId,
        Set<String> roles
) {}