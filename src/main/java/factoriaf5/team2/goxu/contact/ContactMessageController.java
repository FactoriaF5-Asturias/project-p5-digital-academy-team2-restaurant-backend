package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/*
 * Endpoints de los mensajes de contacto
 * La ruta base toma el prefijo de la API desde application.properties (${api-endpoint}),
 * igual que RegisterController
 * El Controller solo recibe la petición y delega el trabajo en el Service
 */
@RestController
@RequestMapping(path = "${api-endpoint}/contact-messages")
public class ContactMessageController {

    /* Inyección por constructor, igual que en el Service*/
    private final ContactMessageService service;

    public ContactMessageController(ContactMessageService service) {
        this.service = service;
    }

    /*
     * POST: guarda un mensaje enviado desde el formulario "¿Hablamos?"
     * @Valid hace que Spring compruebe las validaciones del DTO antes de ejecutar el método:
     * si alguna falla, responde 400 Bad Request sin llegar al Service
     * @RequestBody convierte el JSON recibido en el DTO de petición
     * Responde 201 Created con el mensaje guardado (incluido su id)
     */
    @PostMapping("")
    public ResponseEntity<ContactMessageDTOResponse> createMessage(@Valid @RequestBody ContactMessageDTORequest dto) {
        ContactMessageDTOResponse response = service.createMessage(dto);
        return ResponseEntity.status(201).body(response);
    }

    /*
     * GET: devuelve todos los mensajes, para administración
     * Responde 200 OK con la lista (vacía si no hay mensajes)
     */
    @GetMapping("")
    public ResponseEntity<List<ContactMessageDTOResponse>> getAllMessages() {
        return ResponseEntity.ok(service.getAllMessages());
    }
}