package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

/*
 * Traduce entre EventEntity y sus DTOs, en las dos direcciones.
 * Mismo patrón que el resto de mappers: métodos estáticos y construcción con builder
 */
public class EventMapper {

    /* DTO de petición -> entidad. No se asigna id: lo genera la base de datos */
    public static EventEntity toEntity(EventDTORequest dto) {
        return EventEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .eventDate(dto.eventDate())
                .image(dto.image())
                .details(dto.details())
                .price(dto.price())
                .featured(dto.featured())
                .build();
    }

    /* Entidad guardada -> DTO de respuesta, incluido el id */
    public static EventDTOResponse toDTO(EventEntity entity) {
        return EventDTOResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .eventDate(entity.getEventDate())
                .image(entity.getImage())
                .details(entity.getDetails())
                .price(entity.getPrice())
                .featured(entity.isFeatured())
                .build();
    }
}