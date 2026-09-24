package factoriaf5.team2.goxu.billing.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BillingReportFileDTOResponse {
    private String fileName;
    private String url;
    private LocalDateTime generatedAt;
}