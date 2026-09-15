package factoriaf5.team2.goxu.roles;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.HashSet;

@Service 
public class RoleService {
    
    private final RoleRepository repository;
    private final Long ROLE_BY_DEFAULT = 1L;
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public RoleEntity getById(Long id) {
        return repository.findById(id).orElseThrow(); 
    }
    /* Pendiente excepciones */

    public Set<RoleEntity> assignDefaultRole() {
        RoleEntity defaultRole = this.getById(ROLE_BY_DEFAULT);

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(defaultRole);

        return roles;
    }

}