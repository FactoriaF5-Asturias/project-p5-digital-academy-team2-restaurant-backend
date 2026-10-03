package factoriaf5.team2.goxu.roles;

import factoriaf5.team2.goxu.users.UserEntity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;

import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName name;     /* Sustituyo por un RoleName */

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;
}