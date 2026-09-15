package factoriaf5.team2.goxu.users;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service /* Marco como un bean - gestionado por Spring - de la capa service*/
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional /* atomicidad y rollback automático */
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

}