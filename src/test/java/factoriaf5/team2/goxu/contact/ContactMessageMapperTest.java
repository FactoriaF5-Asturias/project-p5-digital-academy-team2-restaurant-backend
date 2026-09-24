package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Tests unitarios de ContactMessageMapper
 * No arrancan Spring ni la base de datos: solo prueban la conversión entre objetos
 */
class ContactMessageMapperTest {

    /* Los cinco campos del DTO de petición llegan correctamente a la entidad. */
    @Test
    void toEntity_mapsAllFields() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        ContactMessageEntity entity = ContactMessageMapper.toEntity(dto);

        assertEquals("Pelayo Álvarez", entity.getFullName());
        assertEquals("pelayo@ejemplo.com", entity.getEmail());
        assertEquals("+34 600 000 000", entity.getPhone());
        assertEquals("Reserva para grupo especial", entity.getMessage());
        assertEquals(ContactPreference.CALL, entity.getContactPreference());
    }

    /* El mapper NO asigna id: debe generarlo la base de datos al guardar */
    @Test
    void toEntity_doesNotSetId() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        ContactMessageEntity entity = ContactMessageMapper.toEntity(dto);

        assertNull(entity.getId());
    }

    /* Todos los campos de la entidad guardada, incluido el id, llegan al DTO de respuesta */
    @Test
    void toDTO_mapsAllFieldsIncludingId() {
        ContactMessageEntity entity = ContactMessageEntity.builder()
                .id(1L)
                .fullName("Pelayo Álvarez")
                .email("pelayo@ejemplo.com")
                .phone("+34 600 000 000")
                .message("Reserva para grupo especial")
                .contactPreference(ContactPreference.EMAIL)
                .build();

        ContactMessageDTOResponse response = ContactMessageMapper.toDTO(entity);

        assertEquals(1L, response.id());
        assertEquals("Pelayo Álvarez", response.fullName());
        assertEquals("pelayo@ejemplo.com", response.email());
        assertEquals("+34 600 000 000", response.phone());
        assertEquals("Reserva para grupo especial", response.message());
        assertEquals(ContactPreference.EMAIL, response.contactPreference());
    }
}