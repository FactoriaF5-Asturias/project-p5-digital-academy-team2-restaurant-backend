package factoriaf5.team2.goxu.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(name = "supabase.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpCloudStorageService implements CloudStorageService {

    @Override
    public String uploadFile(String fileName, byte[] content, String contentType) {
        throw new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED,
                "El almacenamiento en la nube todavía no está configurado (supabase.enabled=false)");
    }

}