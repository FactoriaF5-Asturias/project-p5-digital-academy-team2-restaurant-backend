package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Lógica de negocio de los eventos
 * Mismo estilo que el resto de Services: inyección por constructor
 * y devolución de DTOs (nunca entidades) al Controller
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /* Todos los eventos ordenados por fecha, para el calendario anual */
    public List<EventDTOResponse> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc()
                .stream()
                .map(EventMapper::toDTO)
                .toList();
    }

    /* Solo los eventos destacados, ordenados por fecha, para la Home */
    public List<EventDTOResponse> getFeaturedEvents() {
        return eventRepository.findByFeaturedTrueOrderByEventDateAsc()
                .stream()
                .map(EventMapper::toDTO)
                .toList();
    }

    /* Crea un evento y lo devuelve con el id generado */
    @Transactional
    public EventDTOResponse createEvent(EventDTORequest dto) {
        EventEntity eventToSave = EventMapper.toEntity(dto);
        EventEntity savedEvent = eventRepository.save(eventToSave);
        return EventMapper.toDTO(savedEvent);
    }
}