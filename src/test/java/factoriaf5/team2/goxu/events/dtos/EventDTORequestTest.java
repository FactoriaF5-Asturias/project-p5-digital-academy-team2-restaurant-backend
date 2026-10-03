package factoriaf5.team2.goxu.events.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Tests de las validaciones de EventDTORequest
 * Se usa directamente un Validator, sin arrancar Spring
 */
class EventDTORequestTest {

    private static Validator validator;

    /* Se ejecuta una sola vez antes de todos los tests; por eso es static */
    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 3, 28, 20, 30);

    /* Un DTO con todos los datos correctos no tiene ninguna violación */
    @Test
    void validRequest_hasNoViolations() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción", DATE, "/events-img/events-pairing.jpeg",
                "18 plazas exclusivas", new BigDecimal("75.00"), true);

        assertTrue(validator.validate(dto).isEmpty());
    }

    /* Evento gratuito y sin detalles: price y details son opcionales, así que es válido */
    @Test
    void freeEventWithoutDetails_isValid() {
        EventDTORequest dto = new EventDTORequest(
                "Mesa Redonda", "Descripción", DATE, "/events-img/events-panel-discussion.jpeg",
                null, null, false);

        assertTrue(validator.validate(dto).isEmpty());
    }

    /* Título vacío: salta @NotBlank */
    @Test
    void blankTitle_isRejected() {
        EventDTORequest dto = new EventDTORequest(
                "", "Descripción", DATE, "/events-img/events-pairing.jpeg", null, null, false);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Descripción de 1001 caracteres, uno más del máximo: salta @Size */
    @Test
    void tooLongDescription_isRejected() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "a".repeat(1001), DATE, "/events-img/events-pairing.jpeg",
                null, null, false);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Sin fecha: salta @NotNull. */
    @Test
    void nullEventDate_isRejected() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción", null, "/events-img/events-pairing.jpeg",
                null, null, false);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Imagen vacía: salta @NotBlank */
    @Test
    void blankImage_isRejected() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción", DATE, "", null, null, false);

        assertFalse(validator.validate(dto).isEmpty());
    }

    /* Precio negativo: salta @PositiveOrZero */
    @Test
    void negativePrice_isRejected() {
        EventDTORequest dto = new EventDTORequest(
                "Cena Maridaje", "Descripción", DATE, "/events-img/events-pairing.jpeg",
                null, new BigDecimal("-5.00"), false);

        assertFalse(validator.validate(dto).isEmpty());
    }
}