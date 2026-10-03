package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
 * Tests unitarios de EventService
 * El repositorio se sustituye por un mock: no se arranca Spring ni la base de datos
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    /* Crea un evento de prueba con el id indicado, para no repetir el builder en cada test */
    private EventEntity buildEvent(Long id, String title, boolean featured) {
        return EventEntity.builder()
                .id(id)
                .title(title)
                .description("Descripción de prueba")
                .eventDate(LocalDateTime.of(2026, 3, 28, 20, 30))
                .image("/events-img/events-pairing.jpeg")
                .details("18 plazas exclusivas")
                .price(new BigDecimal("75.00"))
                .featured(featured)
                .build();
    }

    /* getAllEvents devuelve todos los eventos convertidos en DTOs, en el orden del repositorio */
    @Test
    void getAllEvents_returnsAllEventsAsDTOs() {
        when(eventRepository.findAllByOrderByEventDateAsc()).thenReturn(List.of(
                buildEvent(1L, "Cena Maridaje", true),
                buildEvent(2L, "Noche de Sidra", false)));

        List<EventDTOResponse> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        assertEquals("Cena Maridaje", result.get(0).title());
        assertEquals("Noche de Sidra", result.get(1).title());
    }

    /* getFeaturedEvents usa la consulta de destacados y devuelve sus DTOs */
    @Test
    void getFeaturedEvents_returnsOnlyFeaturedEvents() {
        when(eventRepository.findByFeaturedTrueOrderByEventDateAsc()).thenReturn(List.of(
                buildEvent(1L, "Cena Maridaje", true)));

        List<EventDTOResponse> result = eventService.getFeaturedEvents();

        assertEquals(1, result.size());
        assertTrue(result.get(0).featured());
    }

    /* Caso límite: si no hay eventos, se devuelve una lista vacía (no null) */
    @Test
    void getAllEvents_returnsEmptyListWhenNoEvents() {
        when(eventRepository.findAllByOrderByEventDateAsc()).thenReturn(List.of());

        List<EventDTOResponse> result = eventService.getAllEvents();

        assertTrue(result.isEmpty());
    }

    /* createEvent guarda el evento y lo devuelve con el id generado */
    @Test
    void createEvent_savesAndReturnsResponseWithId() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción de prueba",
                LocalDateTime.of(2026, 3, 28, 20, 30),
                "/events-img/events-pairing.jpeg", "18 plazas exclusivas",
                new BigDecimal("75.00"), true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(buildEvent(1L, "Cena Maridaje", true));

        EventDTOResponse response = eventService.createEvent(dto);

        assertEquals(1L, response.id());
        assertEquals("Cena Maridaje", response.title());
        verify(eventRepository).save(any(EventEntity.class));
    }
}