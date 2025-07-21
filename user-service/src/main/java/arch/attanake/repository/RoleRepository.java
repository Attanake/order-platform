package arch.attanake.repository;

import arch.attanake.entity.RoleEntity;
import arch.attanake.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleType name);
}
