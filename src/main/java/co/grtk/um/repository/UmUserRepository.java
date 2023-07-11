package co.grtk.um.repository;

import co.grtk.um.model.UmUser;
import co.grtk.um.model.PrincipalStatus;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UmUserRepository extends CrudRepository<UmUser,Long> {

    Optional<UmUser> findByName(String name);
    Optional<UmUser> findByEmail(String email);
    Optional<UmUser> findByEmailAndStatus(String email, PrincipalStatus status);

}
