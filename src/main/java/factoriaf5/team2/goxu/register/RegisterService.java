package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;
import factoriaf5.team2.goxu.users.UserEntity;
import factoriaf5.team2.goxu.users.UserRepository;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Falta para ser operativo: 
decrypt, roleservice*/

@Service
public class RegisterService {

    //Inyección del repo y el encoder.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public RegisterDTOResponse registerUser(RegisterDTORequest dto) {

         // 1. Comprueba si existe el email.
        UserEntity probe = new UserEntity();
        probe.setEmail(dto.email());
        Example<UserEntity> example = Example.of(probe);
        if (!userRepository.findAll(example).isEmpty()) {
            return null; // el controller lo traduce a 409 Conflict
        }

        // 2. Mapea el DTO. Hashea la contraseña.
        UserEntity userToSave = RegisterMapper.toEntity(dto);
        userToSave.setPassword(passwordEncoder.encode(dto.password()));

        // 3. Guarda y emite DTO de respuesta
        UserEntity saved = userRepository.save(userToSave);
        return RegisterMapper.toDTO(saved);
    
    }
}