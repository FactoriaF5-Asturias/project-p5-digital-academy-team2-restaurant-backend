package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
 * Tests unitarios de EventController
 * El Service se sustituye por un mock y se llama directamente a los métodos del Controller
 * para comprobar el código de estado y el cuerpo de la respuesta
 */
class EventControllerTest {

    private EventService service;
    private EventController controller;

    /* Se ejecuta antes de cada test, para que cada uno empiece con objetos nuevos */
    @BeforeEach
    void setUp() {
        service = mock(EventService.class);
        controller = new EventController(service);
    }

    /* Crea un DTO de respuesta de prueba, para no repetir el builder en cada test */
    private EventDTOResponse buildResponse(Long id, boolean featured) {
        return EventDTOResponse.builder()
                .id(id)
                .title("Cena Maridaje")
                .description("Descripción de prueba")
                .eventDate(LocalDateTime.of(2026, 3, 28, 20, 30))
                .image("/events-img/events-pairing.jpeg")
                .details("18 plazas exclusivas")
                .price(new BigDecimal("75.00"))
                .featured(featured)
                .build();
    }

    /* GET de todos los eventos: responde 200 con la lista que devuelve el Service */
    @Test
    void getAllEvents_returns200WithEvents() {
        List<EventDTOResponse> events = List.of(buildResponse(1L, true), buildResponse(2L, false));
        when(service.getAllEvents()).thenReturn(events);

        ResponseEntity<List<EventDTOResponse>> response = controller.getAllEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(events, response.getBody());
    }

    /* GET de destacados: responde 200 con la lista de destacados que devuelve el Service */
    @Test
    void getFeaturedEvents_returns200WithFeaturedEvents() {
        List<EventDTOResponse> featuredEvents = List.of(buildResponse(1L, true));
        when(service.getFeaturedEvents()).thenReturn(featuredEvents);

        ResponseEntity<List<EventDTOResponse>> response = controller.getFeaturedEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(featuredEvents, response.getBody());
    }

    /* POST de un evento válido: responde 201 con el evento creado */
    @Test
    void createEvent_returns201WithCreatedEvent() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción de prueba",
                LocalDateTime.of(2026, 3, 28, 20, 30),
                "/events-img/events-pairing.jpeg", "18 plazas exclusivas",
                new BigDecimal("75.00"), true);
        EventDTOResponse expected = buildResponse(1L, true);
        when(service.createEvent(dto)).thenReturn(expected);

        ResponseEntity<EventDTOResponse> response = controller.createEvent(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }
}