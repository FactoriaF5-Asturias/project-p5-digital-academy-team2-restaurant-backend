package factoriaf5.team2.goxu.contact.dtos;

import factoriaf5.team2.goxu.contact.ContactPreference;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Tests de las validaciones de ContactMessageDTORequest */
class ContactMessageDTORequestTest {

    private static Validator validator;

    /* Se ejecuta una sola vez antes de todos los tests; por eso es static*/
    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /* Un DTO con todos los datos correctos no tiene ninguna violación */
    @Test
    void validRequest_hasNoViolations() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        Set<ConstraintViolation<ContactMessageDTORequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /* Nombre vacío: salta @NotBlank */
    @Test
    void blankFullName_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Email con formato incorrecto: salta @Email */
    @Test
    void invalidEmail_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "esto no es un email", "+34 600 000 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Teléfono con letras: salta @Pattern*/
    @Test
    void phoneWithLetters_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "teléfono 600",
                "Reserva para grupo especial", ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Teléfono demasiado corto (menos de 9 caracteres): salta @Pattern */
    @Test
    void tooShortPhone_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "600 000",
                "Reserva para grupo especial", ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Mensaje vacío: salta @NotBlank. */
    @Test
    void blankMessage_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "", ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Mensaje de 1001 caracteres, uno más del máximo: salta @Size */
    @Test
    void tooLongMessage_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "a".repeat(1001), ContactPreference.CALL);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Sin preferencia de contacto: salta @NotNull */
    @Test
    void nullContactPreference_isRejected() {
        ContactMessageDTORequest dto = new ContactMessageDTORequest(
                "Pelayo Álvarez", "pelayo@ejemplo.com", "+34 600 000 000",
                "Reserva para grupo especial", null);

        assertFalse(validator.validate(dto).isEmpty());
    }
}