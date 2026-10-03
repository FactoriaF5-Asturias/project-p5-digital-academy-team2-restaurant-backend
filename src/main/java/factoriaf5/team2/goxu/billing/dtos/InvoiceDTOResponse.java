package factoriaf5.team2.goxu.billing.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import factoriaf5.team2.goxu.orders.dtos.OrderItemDTOResponse;

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
public class InvoiceDTOResponse {

    private Long orderId;
    private String userName;
    private String tableNumber;
    private LocalDateTime createdAt;
    private List<OrderItemDTOResponse> items;
    private BigDecimal total;

}