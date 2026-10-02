package factoriaf5.team2.goxu.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Mensaje enviado desde el formulario de contacto "¿Hablamos?" de la Home
 * Se guarda en la tabla contact_messages
 */
@Entity
@Table(name = "contact_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContactMessageEntity {

    /*
     * Clave primaria autoincremental generada por la base de datos
     * El nombre explícito de la columna sigue la convención de UserEntity (id_user).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact_message")
    private Long id;

    /* Nombre completo de la persona. Obligatorio en la base de datos. */
    @Column(nullable = false)
    private String fullName;

    /*
     * Correo electrónico. A diferencia de UserEntity, NO es unique:
     * una misma persona puede enviar varios mensajes.
     */
    @Column(nullable = false)
    private String email;

    /*
     * Teléfono guardado como texto, no como número,
     * para conservar el prefijo "+" y los espacios.
     */
    @Column(nullable = false)
    private String phone;

    /*
     * Texto del mensaje. Se amplía la longitud a 1000 caracteres,
     * porque por defecto un String se guarda con un máximo de 255.
     */
    @Column(nullable = false, length = 1000)
    private String message;

    /*
     * Preferencia de contacto (CALL o EMAIL).
     * Se guarda como texto en la base de datos, no como número de posición.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactPreference contactPreference;
}