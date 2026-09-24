package factoriaf5.team2.goxu.storage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class NoOpCloudStorageServiceTest {

    private final NoOpCloudStorageService service = new NoOpCloudStorageService();

    @Test
    void uploadFile_shouldThrow_becauseSupabaseIsNotConfigured() {
        assertThatThrownBy(() -> service.uploadFile("test.pdf", new byte[] {1, 2, 3}, "application/pdf"))
                .isInstanceOf(ResponseStatusException.class);
    }

}