package factoriaf5.team2.goxu.dashboard.dtos;

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
public class TopProductDTO {

    private Long productId;
    private String productName;
    private long unitsSold;

}