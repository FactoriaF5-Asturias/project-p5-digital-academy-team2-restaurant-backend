package factoriaf5.team2.goxu.account;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.account.dtos.AccountDTOResponse;
import factoriaf5.team2.goxu.account.dtos.AccountUpdateDTORequest;
import factoriaf5.team2.goxu.account.dtos.ChangePasswordDTORequest;
import factoriaf5.team2.goxu.users.UserEntity;
import factoriaf5.team2.goxu.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountDTOResponse getAccount(Long userId) {
        UserEntity user = findUserOrThrow(userId);
        return toResponse(user);
    }

    public AccountDTOResponse updateAccount(Long userId, AccountUpdateDTORequest request) {
        UserEntity user = findUserOrThrow(userId);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        UserEntity updated = userRepository.save(user);
        return toResponse(updated);
    }

    public void changePassword(Long userId, ChangePasswordDTORequest request) {
        UserEntity user = findUserOrThrow(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserEntity findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + userId));
    }

    private AccountDTOResponse toResponse(UserEntity user) {
        return AccountDTOResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}