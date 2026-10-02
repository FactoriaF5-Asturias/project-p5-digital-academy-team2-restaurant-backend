package factoriaf5.team2.goxu.orders.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import factoriaf5.team2.goxu.orders.OrderStatus;

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
public class OrderDTOResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String tableNumber;
    private OrderStatus status;
    private boolean paid;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemDTOResponse> items;

}