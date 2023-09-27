package co.grtk.um.service;

import co.grtk.um.dto.ProfileDTO;
import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.model.MfaType;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.repository.UmUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UmUserRepository umUserRepository;
    private final ModelMapper modelMapper;

    private static final String ERRORLOG = "User not found email: ";
    @Transactional
    public ProfileDTO saveProfile(String email, ProfileDTO profileDTO) {
        UmUser umUser = umUserRepository
                .findByIdAndEmailAndStatus(profileDTO.getId(),email, UmUserStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "UserProfile not found id:" + profileDTO.getId() + " email: " + email));

        mapUmUserDTO(umUser,profileDTO);
        umUser = umUserRepository.save(umUser);
        return modelMapper.map(umUser,ProfileDTO.class);
    }

    @Transactional
    public ProfileDTO loadUser(String email) throws UserNotFoundException {
        UmUser umUser = umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException(ERRORLOG + email));
        return modelMapper.map(umUser,ProfileDTO.class);
    }

    private void mapUmUserDTO(UmUser umUser, ProfileDTO profileDTO) {
        umUser.setName(profileDTO.getName());
        umUser.setPhone(profileDTO.getPhone());

        boolean isAdmin =
                umUser.getRoles().stream().anyMatch(umUserRole -> "ROLE_ADMIN".equalsIgnoreCase(umUserRole.getName()));
        if(isAdmin) {
            umUser.setMfaType(MfaType.APP);
        } else {
            umUser.setMfaType(profileDTO.getMfaType());
        }
    }

}
