package factoriaf5.team2.goxu.contact.dtos;

import factoriaf5.team2.goxu.contact.ContactPreference;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
 * Datos que envía el formulario "¿Hablamos?" al back (lo que ENTRA en la API)
 * No incluye id: lo genera la base de datos y el cliente no debe poder asignarlo
 * Las validaciones replican las del formulario del front, para que ambos lados
 * acepten y rechacen los mismos datos
 */
public record ContactMessageDTORequest(

        @NotBlank(message = "El nombre es obligatorio")
        String fullName,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es válido")
        String email,

        /*
         * Mismo patrón que el input del front: prefijo "+" opcional
         * y entre 9 y 15 dígitos o espacios.
         * En Java la barra invertida se escribe doble dentro de un String
         */
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?[0-9\\s]{9,15}$",
                message = "El teléfono solo puede contener dígitos, espacios y un prefijo opcional con +")
        String phone,

        /* Máximo de 1000 caracteres, igual que la longitud de la columna en la entidad */
        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres")
        String message,

        /* @NotNull y no @NotBlank: el enum no es un texto, solo puede tener valor o no tenerlo*/
        @NotNull(message = "La preferencia de contacto es obligatoria")
        ContactPreference contactPreference
) {
}