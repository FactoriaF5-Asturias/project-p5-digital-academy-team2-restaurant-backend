package factoriaf5.team2.goxu.register.validation;

import jakarta.validation.Constraint; /* convierte en constraint de Bean Validation */
import jakarta.validation.Payload;  /* adjunta mtadatos a la validación */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = PasswordMatchesValidator.class) /* Uno la anotación con la lógica de la otra clase */
@Target(ElementType.TYPE)           
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Las contraseñas no coinciden, goxu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}