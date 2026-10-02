package factoriaf5.team2.goxu.auth.dtos;

//Respuesta del login.
//Si las credenciales son correctas, backend responderá 200 con el TokenDTO.

public record TokenAuthDTOResponse(String token) {

}
