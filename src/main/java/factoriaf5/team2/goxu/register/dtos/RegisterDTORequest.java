package factoriaf5.team2.goxu.register.dtos;

import factoriaf5.team2.goxu.register.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches 
public record RegisterDTORequest(

        @NotBlank(message = "El nombre es obligatorio")                          
        String name,

        @NotBlank(message = "El correo es obligatorio")                         
        @Email(message = "El formato del correo no es válido")                  
        String email,

        @NotBlank(message = "La contraseña es obligatoria")                     
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") 
        String password,

        @NotBlank(message = "Confirma la contraseña")                           
        String confirmPassword
) {   
}