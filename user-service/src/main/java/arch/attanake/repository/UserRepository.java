package arch.attanake.repository;

import arch.attanake.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
}
