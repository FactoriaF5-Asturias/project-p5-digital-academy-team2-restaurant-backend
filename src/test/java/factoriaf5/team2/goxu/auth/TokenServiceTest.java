package factoriaf5.team2.goxu.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder encoder;

    @InjectMocks
    private TokenService tokenService;
    private final ArgumentCaptor<JwtEncoderParameters> parametersCaptor =
            ArgumentCaptor.forClass(JwtEncoderParameters.class);

    @BeforeEach
    void setUp() {
        Jwt encoded = Jwt.withTokenValue("encoded.jwt.token")
                .header("alg", "HS512")
                .claim("sub", "any")
                .build();
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(encoded);
    }

    @Test
    void generateToken_buildsHs512TokenWithUserDataAndFifteenMinuteExpiry() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "juan@authentication.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"), new SimpleGrantedAuthority("ROLE_ADMIN")));

        String token = tokenService.generateToken(authentication);

        assertEquals("encoded.jwt.token", token);

        JwtEncoderParameters parameters = captureParameters();
        JwtClaimsSet claims = parameters.getClaims();

        assertEquals(MacAlgorithm.HS512, parameters.getJwsHeader().getAlgorithm());
        assertEquals("goxu", claims.getClaimAsString(JwtClaimNames.ISS));
        assertEquals("juan@authentication.com", claims.getSubject());
        assertEquals("ROLE_CUSTOMER ROLE_ADMIN", claims.getClaimAsString("scope"));
        assertEquals(Duration.ofMinutes(15), Duration.between(claims.getIssuedAt(), claims.getExpiresAt()));
    }

    @Test
    void renewToken_keepsSubjectAndScopeOfPreviousToken() {
        Jwt previous = Jwt.withTokenValue("old.jwt.token")
                .header("alg", "HS512")
                .subject("juan@authentication.com")
                .claim("scope", "ROLE_CUSTOMER")
                .build();

        String token = tokenService.renewToken(previous);

        assertEquals("encoded.jwt.token", token);

        JwtClaimsSet claims = captureParameters().getClaims();

        assertEquals("juan@authentication.com", claims.getSubject());
        assertEquals("ROLE_CUSTOMER", claims.getClaimAsString("scope"));
    }

    @Test
    void renewToken_withoutScope_usesEmptyScope() {
        Jwt previous = Jwt.withTokenValue("old.jwt.token")
                .header("alg", "HS512")
                .subject("juan@authentication.com")
                .build();

        tokenService.renewToken(previous);

        assertEquals("", captureParameters().getClaims().getClaimAsString("scope"));
    }
    private JwtEncoderParameters captureParameters() {
        verify(encoder).encode(parametersCaptor.capture());
        return parametersCaptor.getValue();
    }
}