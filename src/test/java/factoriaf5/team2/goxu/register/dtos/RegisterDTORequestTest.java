package factoriaf5.team2.goxu.register.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterDTORequestTest {

    private static Validator validator;
    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test // Si el DTO es correcto, no se da ningún error.
    void validRequest_hasNoViolations() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "password123");

        Set<ConstraintViolation<RegisterDTORequest>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty()); 
    }
    @Test // Si el espacio de name va vacío, salta un error.
    void blankName_isRejected() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "", "juan@goxu.com", "password123", "password123");

        assertFalse(validator.validate(dto).isEmpty()); // @NotBlank salta
    }
    @Test // Si no se cumple el formato @email da error.
    void invalidEmail_isRejected() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "Esto no es un email", "password123", "password123");

        assertFalse(validator.validate(dto).isEmpty()); // @Email salta
    }
    @Test //Si la contraseña es demasiado corta (inferior a 8 caracteres), da error.
    void shortPassword_isRejected() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "1234567", "1234567"); // 7 caracteres

        assertFalse(validator.validate(dto).isEmpty()); 
    }
    @Test //Si la contraseña es distinta, da error.
    void mismatchedPasswords_areRejected() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "otraDistinta");

        assertFalse(validator.validate(dto).isEmpty()); 
    }
}