package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Tests unitarios de EventMapper.
 * No arrancan Spring ni la base de datos: solo prueban la conversión entre objetos
 */
class EventMapperTest {

    /* Los campos del DTO de petición llegan correctamente a la entidad */
    @Test
    void toEntity_mapsAllFields() {
        LocalDateTime date = LocalDateTime.of(2026, 3, 28, 20, 30);
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Recorrido por 6 llagares históricos", date,
                "/events-img/events-pairing.jpeg", "18 plazas exclusivas",
                new BigDecimal("75.00"), true);

        EventEntity entity = EventMapper.toEntity(dto);

        assertEquals("Cena Maridaje", entity.getTitle());
        assertEquals("Recorrido por 6 llagares históricos", entity.getDescription());
        assertEquals(date, entity.getEventDate());
        assertEquals("/events-img/events-pairing.jpeg", entity.getImage());
        assertEquals("18 plazas exclusivas", entity.getDetails());
        assertEquals(new BigDecimal("75.00"), entity.getPrice());
        assertTrue(entity.isFeatured());
    }

    /* El mapper NO asigna id: debe generarlo la base de datos */
    @Test
    void toEntity_doesNotSetId() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Recorrido por 6 llagares históricos",
                LocalDateTime.of(2026, 3, 28, 20, 30),
                "/events-img/events-pairing.jpeg", null, null, false);

        EventEntity entity = EventMapper.toEntity(dto);

        assertNull(entity.getId());
    }

    /* Caso de evento gratuito: un precio null se mantiene null, no se convierte en 0 */
    @Test
    void toEntity_keepsNullPriceForFreeEvents() {
        EventDTORequest dto = new EventDTORequest(
                "Mesa Redonda", "Diálogo abierto con criadores y llagareros",
                LocalDateTime.of(2026, 4, 24, 19, 0),
                "/events-img/events-panel-discussion.jpeg", "Coloquio & Cóctel de cierre",
                null, true);

        EventEntity entity = EventMapper.toEntity(dto);

        assertNull(entity.getPrice());
    }

    /* Todos los campos de la entidad guardada, incluido el id, llegan al DTO de respuesta */
    @Test
    void toDTO_mapsAllFieldsIncludingId() {
        LocalDateTime date = LocalDateTime.of(2026, 6, 15, 21, 0);
        EventEntity entity = EventEntity.builder()
                .id(4L)
                .title("Noche de Sidra Bajo las Estrellas")
                .description("Cena al aire libre en nuestra terraza")
                .eventDate(date)
                .image("/events-img/events-pairing.jpeg")
                .details("Terraza exterior")
                .price(new BigDecimal("55.00"))
                .featured(false)
                .build();

        EventDTOResponse response = EventMapper.toDTO(entity);

        assertEquals(4L, response.id());
        assertEquals("Noche de Sidra Bajo las Estrellas", response.title());
        assertEquals(date, response.eventDate());
        assertEquals("Terraza exterior", response.details());
        assertEquals(new BigDecimal("55.00"), response.price());
        assertEquals(false, response.featured());
    }
}