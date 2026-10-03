package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Tests unitarios de ContactMessageService
 * El repositorio se sustituye por un mock (versión falsa), así que no se arranca
 * Spring ni la base de datos: solo se prueba la lógica del Service
 * @ExtendWith(MockitoExtension.class) activa Mockito en esta clase de test
 */
@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    /* Repositorio falso: responde lo que le indiquemos en cada test */
    @Mock
    private ContactMessageRepository contactMessageRepository;

    /* Service real, al que Mockito entrega el repositorio falso por el constructor*/
    @InjectMocks
    private ContactMessageService contactMessageService;

    /* Al crear un mensaje, se guarda en el repositorio y se devuelve con el id generado */
    @Test
    void createMessage_savesAndReturnsResponseWithId() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        /* Simulamos lo que haría la base de datos: devolver la entidad ya con id */
        ContactMessageEntity savedEntity = ContactMessageEntity.builder()
                .id(1L)
                .fullName("Pelayo Álvarez")
                .email("pelayo@ejemplo.com")
                .phone("+34 600 000 000")
                .message("Reserva para grupo especial")
                .contactPreference(ContactPreference.CALL)
                .build();
        when(contactMessageRepository.save(any(ContactMessageEntity.class))).thenReturn(savedEntity);

        ContactMessageDTOResponse response = contactMessageService.createMessage(dto);

        assertEquals(1L, response.id());
        assertEquals("Pelayo Álvarez", response.fullName());
        assertEquals(ContactPreference.CALL, response.contactPreference());
        verify(contactMessageRepository).save(any(ContactMessageEntity.class));
    }

    /* Se devuelven todos los mensajes guardados, convertidos en DTOs de respuesta */
    @Test
    void getAllMessages_returnsAllMessagesAsDTOs() {
        ContactMessageEntity firstMessage = ContactMessageEntity.builder()
                .id(1L)
                .fullName("Pelayo Álvarez")
                .email("pelayo@ejemplo.com")
                .phone("+34 600 000 000")
                .message("Reserva para grupo especial")
                .contactPreference(ContactPreference.CALL)
                .build();
        ContactMessageEntity secondMessage = ContactMessageEntity.builder()
                .id(2L)
                .fullName("Xana Fernández")
                .email("xana@ejemplo.com")
                .phone("600 111 222")
                .message("Menú para celiacos")
                .contactPreference(ContactPreference.EMAIL)
                .build();
        when(contactMessageRepository.findAll()).thenReturn(List.of(firstMessage, secondMessage));

        List<ContactMessageDTOResponse> result = contactMessageService.getAllMessages();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Xana Fernández", result.get(1).fullName());
    }

    /* Caso límite: si no hay mensajes, se devuelve una lista vacía (no null) */
    @Test
    void getAllMessages_returnsEmptyListWhenNoMessages() {
        when(contactMessageRepository.findAll()).thenReturn(List.of());

        List<ContactMessageDTOResponse> result = contactMessageService.getAllMessages();

        assertTrue(result.isEmpty());
    }
}