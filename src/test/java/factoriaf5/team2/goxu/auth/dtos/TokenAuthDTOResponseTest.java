package factoriaf5.team2.goxu.auth.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TokenAuthDTOResponseTest {

    @Test
    void token_returnsTheValuePassedInConstructor() {
        
        TokenAuthDTOResponse response = new TokenAuthDTOResponse("fake.jwt.token");

        assertEquals("fake.jwt.token", response.token());
    }
}