package factoriaf5.team2.goxu.contact.dtos;

import factoriaf5.team2.goxu.contact.ContactPreference;

/*
 * Datos que envía el formulario "¿Hablamos?" al back (lo que ENTRA en la API)
 * No incluye id: lo genera la base de datos y el cliente no debe poder asignarlo
 */
public record ContactMessageDTORequest(
        String fullName,
        String email,
        String phone,
        String message,
        ContactPreference contactPreference
) {
}