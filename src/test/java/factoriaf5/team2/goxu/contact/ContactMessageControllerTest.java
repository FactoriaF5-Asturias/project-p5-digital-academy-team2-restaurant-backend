package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * Tests unitarios de ContactMessageController
 * Siguen el estilo de RegisterControllerTest: el Service se sustituye por un mock
 * y se llama directamente a los métodos del Controller para comprobar
 * el código de estado y el cuerpo de la respuesta
 */
class ContactMessageControllerTest {

    private ContactMessageService service;
    private ContactMessageController controller;

    /* Se ejecuta antes de cada test, para que cada uno empiece con objetos nuevos*/
    @BeforeEach
    void setUp() {
        service = mock(ContactMessageService.class);
        controller = new ContactMessageController(service);
    }

    /* Al enviar un mensaje válido, se responde 201 Created con el mensaje guardado */
    @Test
    void createMessage_returns201WithCreatedMessage() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);
        ContactMessageDTOResponse expected = ContactMessageDTOResponse.builder()
                .id(1L)
                .fullName("Pelayo Álvarez")
                .email("pelayo@ejemplo.com")
                .phone("+34 600 000 000")
                .message("Reserva para grupo especial")
                .contactPreference(ContactPreference.CALL)
                .build();
        when(service.createMessage(dto)).thenReturn(expected);

        ResponseEntity<ContactMessageDTOResponse> response = controller.createMessage(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    /* Al pedir los mensajes, se responde 200 OK con la lista que devuelve el Service */
    @Test
    void getAllMessages_returns200WithMessages() {
        ContactMessageDTOResponse message = ContactMessageDTOResponse.builder()
                .id(1L)
                .fullName("Pelayo Álvarez")
                .email("pelayo@ejemplo.com")
                .phone("+34 600 000 000")
                .message("Reserva para grupo especial")
                .contactPreference(ContactPreference.CALL)
                .build();
        when(service.getAllMessages()).thenReturn(List.of(message));

        ResponseEntity<List<ContactMessageDTOResponse>> response = controller.getAllMessages();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(message), response.getBody());
    }
}