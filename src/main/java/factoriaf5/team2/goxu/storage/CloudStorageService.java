package factoriaf5.team2.goxu.storage;

public interface CloudStorageService {

    String uploadFile(String fileName, byte[] content, String contentType);

}