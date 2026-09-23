package factoriaf5.team2.goxu.contact;

import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTORequest;
import factoriaf5.team2.goxu.contact.dtos.ContactMessageDTOResponse;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Lógica de negocio de los mensajes de contacto */
@Service
public class ContactMessageService {

    /*
     * Inyección por constructor: Spring entrega automáticamente el repositorio
     * al crear el Service. El campo es final para que no pueda reasignarse
     */
    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    /*
     * Guarda un mensaje enviado desde el formulario (POST)
     * DTO de petición -> entidad -> guardado en BD -> DTO de respuesta con el id generado
     * @Transactional: si el guardado falla, no queda nada a medias en la base de datos
     */
    @Transactional
    public ContactMessageDTOResponse createMessage(ContactMessageDTORequest dto) {
        ContactMessageEntity messageToSave = ContactMessageMapper.toEntity(dto);
        ContactMessageEntity savedMessage = contactMessageRepository.save(messageToSave);
        return ContactMessageMapper.toDTO(savedMessage);
    }

    /*
     * Devuelve todos los mensajes guardados, para administración (GET)
     * Cada entidad se convierte en DTO de respuesta mediante el mapper
     */
    public List<ContactMessageDTOResponse> getAllMessages() {
        return contactMessageRepository.findAll()
                .stream()
                .map(ContactMessageMapper::toDTO)
                .toList();
    }
}