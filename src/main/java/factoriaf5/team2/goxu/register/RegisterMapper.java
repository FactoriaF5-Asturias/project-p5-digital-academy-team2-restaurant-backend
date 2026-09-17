package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;
import factoriaf5.team2.goxu.users.UserEntity;

public class RegisterMapper {

    // DTO de entidad. NO copia la contraseña.
    public static UserEntity toEntity(RegisterDTORequest dto) {
        return UserEntity.builder()
                .name(dto.name())
                .email(dto.email())
                .build();
    }
    // Entidad guardada. DTO de respuesta
    public static RegisterDTOResponse toDTO(UserEntity user) {
        return RegisterDTOResponse.builder()
                .message("User stored successfully")
                .build();
    }
}