package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;

/*
 * Traduce entre la entidad ContactMessageEntity y sus DTOs, en las dos direcciones
  */
public class ContactMessageMapper {

    /*
     * DTO de petición -> entidad, para poder guardarla en la base de datos
     * No se asigna id: lo genera la base de datos al guardar
     */
    public static ContactMessageEntity toEntity(ContactMessageDTORequest dto) {
        return ContactMessageEntity.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .message(dto.message())
                .contactPreference(dto.contactPreference())
                .build();
    }

    /*
     * Entidad guardada -> DTO de respuesta, para devolverla al cliente
     * Aquí sí se incluye el id, que ya existe porque la entidad está guardada
     */
    public static ContactMessageDTOResponse toDTO(ContactMessageEntity entity) {
        return ContactMessageDTOResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .message(entity.getMessage())
                .contactPreference(entity.getContactPreference())
                .build();
    }
}