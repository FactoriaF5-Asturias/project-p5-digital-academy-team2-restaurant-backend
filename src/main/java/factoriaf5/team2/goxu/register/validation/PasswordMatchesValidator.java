package factoriaf5.team2.goxu.register.validation;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// La clase recibe el DTORequest y compara password

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterDTORequest> {

    @Override
    public boolean isValid(RegisterDTORequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String password = request.password();               // acceso al record de la contraseña
        String confirmPassword = request.confirmPassword();

        if (password == null || confirmPassword == null) {
            return true;
        }

        boolean matches = password.equals(confirmPassword);

        // Si no coinciden, aparece el error del campo confirmPassword 
        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return matches;
    }
}