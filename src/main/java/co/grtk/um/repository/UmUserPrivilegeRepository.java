package co.grtk.um.repository;

import co.grtk.um.model.UmUserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UmUserPrivilegeRepository extends JpaRepository<UmUserPrivilege, Long> {
    Optional<UmUserPrivilege> findByName(String name);
}
