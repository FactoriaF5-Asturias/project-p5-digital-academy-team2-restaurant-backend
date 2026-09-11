package factoriaf5.team2.goxu.register;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;

import org.springframework.stereotype.Service;

/* Falta para ser operativo: 
decrypt, encrypt, roleservice, entidad usuario */

@Service
public class RegisterService {

    public RegisterDTOResponse registerUser(RegisterDTORequest dto) {

        return RegisterDTOResponse.builder().message("User stored successfully").build();
    }
/* Incompleto */
}