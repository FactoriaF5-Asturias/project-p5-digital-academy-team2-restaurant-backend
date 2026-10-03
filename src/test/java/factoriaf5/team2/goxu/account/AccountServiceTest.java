package factoriaf5.team2.goxu.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.account.dtos.AccountDTOResponse;
import factoriaf5.team2.goxu.account.dtos.AccountUpdateDTORequest;
import factoriaf5.team2.goxu.account.dtos.ChangePasswordDTORequest;
import factoriaf5.team2.goxu.users.UserEntity;
import factoriaf5.team2.goxu.users.UserRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAccount_shouldReturnAccount_whenUserExists() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Cliente Test")
                .email("cliente@test.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        AccountDTOResponse result = accountService.getAccount(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Cliente Test");
        assertThat(result.getEmail()).isEqualTo("cliente@test.com");
    }

    @Test
    void getAccount_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateAccount_shouldUpdateNameAndEmail() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Nombre Viejo")
                .email("viejo@test.com")
                .build();

        AccountUpdateDTORequest request = new AccountUpdateDTORequest("Nombre Nuevo", "nuevo@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        AccountDTOResponse result = accountService.updateAccount(1L, request);

        assertThat(result.getName()).isEqualTo("Nombre Nuevo");
        assertThat(result.getEmail()).isEqualTo("nuevo@test.com");
    }

    @Test
    void changePassword_shouldEncodeAndSaveNewPassword_whenCurrentPasswordMatches() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("Cliente Test")
                .email("cliente@test.com")
                .password("hashedOldPassword")
                .build();

        ChangePasswordDTORequest request = new ChangePasswordDTORequest("oldPassword", "newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "hashedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("hashedNewPassword");

        accountService.changePassword(1L, request);

        assertThat(user.getPassword()).isEqualTo("hashedNewPassword");
    }

    @Test
    void changePassword_shouldThrow_whenCurrentPasswordDoesNotMatch() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .password("hashedOldPassword")
                .build();

        ChangePasswordDTORequest request = new ChangePasswordDTORequest("wrongPassword", "newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedOldPassword")).thenReturn(false);

        assertThatThrownBy(() -> accountService.changePassword(1L, request))
                .isInstanceOf(ResponseStatusException.class);
    }

}