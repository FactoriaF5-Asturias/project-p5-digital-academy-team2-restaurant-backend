package factoriaf5.team2.goxu.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Evento gastronómico del restaurante (cenas maridaje, jornadas, coloquios...)
 * Se muestra en "Próximos eventos" de la Home (los destacados) y en el calendario anual
 * Sigue el mismo estilo que ProductEntity y ContactMessageEntity
 */
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventEntity {

    /* Clave primaria autoincremental */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long id;

    @Column(nullable = false)
    private String title;

    /* Descripción ampliada a 1000 caracteres */
    @Column(nullable = false, length = 1000)
    private String description;

    /*
     * Fecha y hora reales del evento, no un texto formateado
     * Permite ordenar por fecha y agrupar por meses en el calendario;
     * el front se encarga de mostrarla con el formato "28 MARZO · 20:30H".
     */
    @Column(nullable = false)
    private LocalDateTime eventDate;

    /* Ruta de la imagen */
    @Column(nullable = false)
    private String image;

    /*
     * Detalle opcional del evento: "18 plazas exclusivas", "Terraza exterior"...
     * Es la parte descriptiva del antiguo campo meta del front
     */
    private String details;

    /*
     * Precio por persona. BigDecimal para evitar errores de redondeo con dinero
     * Opcional: si es null, el evento es gratuito y el front muestra "Entrada gratuita"
     */
    private BigDecimal price;

    /* Si el evento aparece en "Próximos eventos" de la Home */
    @Column(nullable = false)
    private boolean featured;
}