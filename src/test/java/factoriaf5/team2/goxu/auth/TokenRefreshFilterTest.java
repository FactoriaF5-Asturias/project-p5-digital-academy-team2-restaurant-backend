package factoriaf5.team2.goxu.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class TokenRefreshFilterTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private TokenRefreshFilter tokenRefreshFilter;

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void jwtAuthentication_addsRefreshedTokenHeaderAndContinuesChain() throws Exception {
      
        Jwt jwt = Jwt.withTokenValue("old.jwt.token")
                .header("alg", "HS512")
                .subject("juan@authentication.com")
                .claim("scope", "CUSTOMER")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
        when(tokenService.renewToken(jwt)).thenReturn("new.jwt.token");

        tokenRefreshFilter.doFilterInternal(request, response, filterChain);

        assertEquals("new.jwt.token", response.getHeader(TokenRefreshFilter.HEADER));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void noJwtAuthentication_doesNotAddHeaderAndContinuesChain() throws Exception {
    
        tokenRefreshFilter.doFilterInternal(request, response, filterChain);

        assertNull(response.getHeader(TokenRefreshFilter.HEADER));
        verifyNoInteractions(tokenService);
        verify(filterChain).doFilter(request, response);
    }
}