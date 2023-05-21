package co.grtk.um.service;

import co.grtk.um.exception.UserAlreadyExistsException;
import co.grtk.um.model.User;
import co.grtk.um.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(User user) throws UserAlreadyExistsException {
        userRepository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new UserAlreadyExistsException("User Already Exists");
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
    }
}
