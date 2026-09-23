package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;
import factoriaf5.team2.goxu.roles.RoleService;
import factoriaf5.team2.goxu.users.UserEntity;
import factoriaf5.team2.goxu.users.UserRepository;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {

    //Inyección del repo y el encoder y el servicio de roles.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder, 
        RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
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

        // 3. Asigna el rol por defecto (customer)
        userToSave.setRoles(roleService.assignDefaultRole());
        
        // 4. Guarda y emite DTO de respuesta
        UserEntity saved = userRepository.save(userToSave);
        return RegisterMapper.toDTO(saved);
    
    }
}