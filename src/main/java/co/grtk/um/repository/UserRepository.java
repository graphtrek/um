package co.grtk.um.repository;

import co.grtk.um.model.User;
import co.grtk.um.model.UserStatus;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndUserStatus(String email, UserStatus userstatus);

}
