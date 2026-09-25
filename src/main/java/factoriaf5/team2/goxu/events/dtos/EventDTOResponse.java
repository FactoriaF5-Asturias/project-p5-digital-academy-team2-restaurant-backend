package factoriaf5.team2.goxu.events.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

/*
 * Datos que devuelve la API sobre un evento (lo que SALE de la API)
 * Incluye el id generado. @Builder sigue el estilo del resto de DTOs de respuesta
 */
@Builder
public record EventDTOResponse(
        Long id,
        String title,
        String description,
        LocalDateTime eventDate,
        String image,
        String details,
        BigDecimal price,
        boolean featured
) {
}
