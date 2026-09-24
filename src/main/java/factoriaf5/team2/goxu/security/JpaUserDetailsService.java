package factoriaf5.team2.goxu.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import factoriaf5.team2.goxu.users.UserRepository;

@Service // Unirá Spring Security con base de datos, cargando el usuario que se autentica
public class JpaUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override 
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
