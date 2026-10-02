package factoriaf5.team2.goxu.events;

import factoriaf5.team2.goxu.events.dtos.EventDTORequest;
import factoriaf5.team2.goxu.events.dtos.EventDTOResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/*
 * Endpoints de los eventos
 * La ruta base toma el prefijo de la API desde application.properties (${api-endpoint})
 */
@RestController
@RequestMapping(path = "${api-endpoint}/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    /* GET /api/events: todos los eventos ordenados por fecha, para el calendario anual */
    @GetMapping("")
    public ResponseEntity<List<EventDTOResponse>> getAllEvents() {
        return ResponseEntity.ok(service.getAllEvents());
    }

    /* GET /api/events/featured: solo los destacados, para "Próximos eventos" de la Home */
    @GetMapping("/featured")
    public ResponseEntity<List<EventDTOResponse>> getFeaturedEvents() {
        return ResponseEntity.ok(service.getFeaturedEvents());
    }

    /*
     * POST /api/events: crea un evento.
     * @Valid comprueba las validaciones del DTO antes de ejecutar el método (400 si fallan)
     * Responde 201 Created con el evento creado, incluido su id.
     */
    @PostMapping("")
    public ResponseEntity<EventDTOResponse> createEvent(@Valid @RequestBody EventDTORequest dto) {
        EventDTOResponse response = service.createEvent(dto);
        return ResponseEntity.status(201).body(response);
    }
}