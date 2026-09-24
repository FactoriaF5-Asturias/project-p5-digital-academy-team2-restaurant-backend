package factoriaf5.team2.goxu.auth;



import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import factoriaf5.team2.goxu.auth.dtos.TokenAuthDTOResponse;

@RestController
@RequestMapping(path = "${api-endpoint}/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // Flujo I: cuando llega aquí, Spring YA ha comprobado el email y la contraseña con la base de datos.
    @PostMapping("/token")
    public ResponseEntity<TokenAuthDTOResponse> token(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new TokenAuthDTOResponse(token));
    }

    // Flujo II: solo responde con un token válido y devuelve quién eres.
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(Authentication authentication) {
        return ResponseEntity.ok(Map.of("email", authentication.getName()));
    }
}