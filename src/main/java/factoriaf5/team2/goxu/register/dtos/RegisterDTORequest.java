package factoriaf5.team2.goxu.register.dtos;
public record RegisterDTORequest(
    String name, 
    String email,
    String password,
    String confirmPassword
) {   
}