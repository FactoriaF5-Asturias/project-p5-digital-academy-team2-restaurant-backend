package factoriaf5.team2.goxu.products.dtos;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTOResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    private String category;
    private boolean available;
    private boolean featured;
    private String badgeLabel;
    private String badgeTone;

}