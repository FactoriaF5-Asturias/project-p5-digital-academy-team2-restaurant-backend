package factoriaf5.team2.goxu.register;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import factoriaf5.team2.goxu.register.dtos.RegisterDTORequest;
import factoriaf5.team2.goxu.register.dtos.RegisterDTOResponse;


@RestController
@RequestMapping(path = "${api-endpoint}/register")
public class RegisterController {

    private final RegisterService service;

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<RegisterDTOResponse> registerUser(@RequestBody RegisterDTORequest dto) {

        return ResponseEntity.status(201).body(service.registerUser(dto));
        
    }

}