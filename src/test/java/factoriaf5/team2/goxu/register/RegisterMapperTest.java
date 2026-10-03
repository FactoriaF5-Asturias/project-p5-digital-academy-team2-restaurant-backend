package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;
import factoriaf5.team2.goxu.users.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterMapperTest {

    @Test // Los campos que deben copiarse - no password - llegan bien a entity.
    void toEntity_mapsNameAndEmail() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "password123");

        UserEntity entity = RegisterMapper.toEntity(dto);

        assertEquals("Juan", entity.getName());
        assertEquals("juan@goxu.com", entity.getEmail());
    }
    @Test // Comprueba que NO se copie la contraseña.
    void toEntity_doesNotCopyPassword() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "password123");

        UserEntity entity = RegisterMapper.toEntity(dto);

        assertNull(entity.getPassword()); 
    }

    @Test // Comprueba que la respuesta no es nula y remite mensaje.
    void toDTO_returnsResponseWithMessage() {
        UserEntity user = UserEntity.builder()
                .name("Juan").email("juan@goxu.com").build();

        RegisterDTOResponse response = RegisterMapper.toDTO(user);

        assertNotNull(response);
        assertEquals("Usuario creado correctamente. Oink.", response.message());
    }
}