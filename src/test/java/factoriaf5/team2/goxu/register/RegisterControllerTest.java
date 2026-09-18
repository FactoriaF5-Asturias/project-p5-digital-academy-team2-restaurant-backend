package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    private RegisterService service;
    private RegisterController controller;

    @BeforeEach
    void setUp() {
        service = mock(RegisterService.class);          
        controller = new RegisterController(service);  
    }

    @Test // Testea envío DTORequest y DTOResponse esperado, debiendo devolver un 201 (succesfully created).
    void newUser_returns201() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "password123");
        RegisterDTOResponse expected = RegisterDTOResponse.builder()
                .message("User stored successfully").build();
        when(service.registerUser(dto)).thenReturn(expected);   

        ResponseEntity<RegisterDTOResponse> response = controller.registerUser(dto);

        assertEquals(201, response.getStatusCode().value());    
        assertEquals(expected, response.getBody());             
    }

    @Test //Igual, pero aquí esperamos un 409 por email repetido.
    void duplicateEmail_returns409() {
        RegisterDTORequest dto = new RegisterDTORequest(
                "Juan", "juan@goxu.com", "password123", "password123");
        when(service.registerUser(dto)).thenReturn(null);       

        ResponseEntity<RegisterDTOResponse> response = controller.registerUser(dto);

        assertEquals(409, response.getStatusCode().value());    
    }
}