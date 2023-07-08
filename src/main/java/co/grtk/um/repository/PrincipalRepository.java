package co.grtk.um.repository;

import co.grtk.um.model.Principal;
import co.grtk.um.model.PrincipalStatus;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface PrincipalRepository extends CrudRepository<Principal,Long> {

    Optional<Principal> findByName(String name);
    Optional<Principal> findByEmail(String email);
    Optional<Principal> findByEmailAndStatus(String email, PrincipalStatus status);

}
