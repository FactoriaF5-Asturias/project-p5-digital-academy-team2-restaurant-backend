package factoriaf5.team2.goxu.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import factoriaf5.team2.goxu.account.dtos.AccountDTOResponse;
import factoriaf5.team2.goxu.account.dtos.AccountUpdateDTORequest;
import factoriaf5.team2.goxu.account.dtos.ChangePasswordDTORequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AccountDTOResponse> getAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAccount(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AccountDTOResponse> updateAccount(
            @PathVariable Long userId,
            @Valid @RequestBody AccountUpdateDTORequest dto) {

        return ResponseEntity.ok(service.updateAccount(userId, dto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordDTORequest dto) {

        service.changePassword(userId, dto);
        return ResponseEntity.noContent().build();
    }

}