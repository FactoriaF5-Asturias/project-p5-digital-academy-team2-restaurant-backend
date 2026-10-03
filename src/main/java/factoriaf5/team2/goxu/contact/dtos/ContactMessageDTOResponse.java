package factoriaf5.team2.goxu.contact.dtos;

import factoriaf5.team2.goxu.contact.ContactPreference;

import lombok.Builder;

/*
 * Datos que devuelve la API sobre un mensaje de contacto (lo que SALE de la API)
 * Incluye el id generado, para que administración pueda identificar cada mensaje
 * @Builder sigue el mismo estilo que RegisterDTOResponse
 */
@Builder
public record ContactMessageDTOResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String message,
        ContactPreference contactPreference
) {
}