package factoriaf5.team2.goxu.roles;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.HashSet;

@Service 
public class RoleService {
    
    private final RoleRepository repository;
    //cambio el id(1L) porque lo genera la base de datos (long - RoleName)
    private static final RoleName DEFAULT_ROLE = RoleName.CUSTOMER;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public RoleEntity getById(Long id) {
        return repository.findById(id).orElseThrow(); 
    }
        
    public RoleEntity getByName(RoleName name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalStateException(
                        "Role " + name + " not found. Check that data.sql has been executed"));
    }
    // lo mismo, cambio de getById por getByName.
    public Set<RoleEntity> assignDefaultRole() {
        RoleEntity defaultRole = this.getByName(DEFAULT_ROLE);

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(defaultRole);

        return roles;
    }

}