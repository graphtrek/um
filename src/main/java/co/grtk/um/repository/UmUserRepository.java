package co.grtk.um.repository;

import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserStatus;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UmUserRepository extends CrudRepository<UmUser,Long> {

    Optional<UmUser> findByName(String name);
    Optional<UmUser> findByEmail(String email);

    Optional<UmUser> findByIdAndEmail(long id, String email);
    Optional<UmUser> findByEmailAndStatus(String email, UmUserStatus status);

    Optional<UmUser> findByIdAndEmailAndStatus(long id, String email, UmUserStatus status);

}
