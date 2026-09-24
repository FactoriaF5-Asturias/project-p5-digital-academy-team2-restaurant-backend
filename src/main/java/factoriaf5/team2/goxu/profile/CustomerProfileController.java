package factoriaf5.team2.goxu.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import factoriaf5.team2.goxu.profile.dtos.CustomerProfileDTOResponse;

@RestController
@RequestMapping(path = "${api-endpoint}/profile")
public class CustomerProfileController {

    private final CustomerProfileService service;

    public CustomerProfileController(CustomerProfileService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CustomerProfileDTOResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getProfile(userId));
    }

}