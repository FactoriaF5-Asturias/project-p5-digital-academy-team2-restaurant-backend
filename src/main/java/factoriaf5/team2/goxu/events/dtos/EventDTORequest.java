package factoriaf5.team2.goxu.events.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/*
 * Datos para crear un evento (lo que ENTRA en la API).
 * No incluye id: lo genera la base de datos
 */
public record EventDTORequest(

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String description,

        /* @NotNull y no @NotBlank: una fecha no es un texto */
        @NotNull(message = "La fecha del evento es obligatoria")
        LocalDateTime eventDate,

        @NotBlank(message = "La imagen es obligatoria")
        String image,

        /* Opcional: "18 plazas exclusivas", "Terraza exterior"... */
        String details,

        /* Opcional: si es null, el evento es gratuito. Si se indica, no puede ser negativo */
        @PositiveOrZero(message = "El precio no puede ser negativo")
        BigDecimal price,

        /* Si no se envía, vale false. */
        boolean featured
) {
}