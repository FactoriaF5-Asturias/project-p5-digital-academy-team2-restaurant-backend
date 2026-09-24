package factoriaf5.team2.goxu.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(name = "supabase.enabled", havingValue = "true")
public class SupabaseStorageService implements CloudStorageService {

    private final RestClient restClient;
    private final String supabaseUrl;
    private final String storageBucket;

    public SupabaseStorageService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.secret-key}") String secretKey,
            @Value("${supabase.storage-bucket}") String storageBucket) {

        this.supabaseUrl = supabaseUrl;
        this.storageBucket = storageBucket;
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl + "/storage/v1")
                .defaultHeader("Authorization", "Bearer " + secretKey)
                .build();
    }

    @Override
    public String uploadFile(String fileName, byte[] content, String contentType) {
        try {
            restClient.post()
                    .uri("/object/{bucket}/{fileName}", storageBucket, fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(content)
                    .retrieve()
                    .toBodilessEntity();

            return supabaseUrl + "/storage/v1/object/public/" + storageBucket + "/" + fileName;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir el archivo a Supabase Storage", e);
        }
    }

}