package factoriaf5.team2.goxu.contact;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Acceso a la tabla contact_messages
 * Al extender JpaRepository se heredan automáticamente los métodos básicos,
 * entre ellos los dos que necesita esta feature:
 *   - save():    guardar un mensaje enviado desde el formulario (POST).
 *   - findAll(): listar todos los mensajes para administración (GET).
 * Por eso no se declara ningún método propio
 *
 * <ContactMessageEntity, Long>: entidad que gestiona y tipo de su clave primaria
 */
public interface ContactMessageRepository extends JpaRepository<ContactMessageEntity, Long> {

}