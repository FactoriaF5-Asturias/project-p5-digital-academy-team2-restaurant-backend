package factoriaf5.team2.goxu.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import factoriaf5.team2.goxu.users.UserEntity;

// Adapto el UserEntity al modelo que entiende Spring Security
public class SecurityUser implements UserDetails {
    
    private final UserEntity user;
    public SecurityUser(UserEntity user) {
        this.user = user;
    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override // Convertirá cada rol en un nivel de autorización
    public Collection<? extends GrantedAuthority> getAuthorities() {
         if (user.getRoles() == null) {
            return List.of(); 
        }
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}

