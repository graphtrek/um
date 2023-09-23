package co.grtk.um.repository;

import co.grtk.um.model.UmUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UmUserRoleRepository extends JpaRepository<UmUserRole, Long> {

    Optional<UmUserRole> findByName(String name);
}
