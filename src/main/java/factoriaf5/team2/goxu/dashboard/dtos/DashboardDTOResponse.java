package factoriaf5.team2.goxu.dashboard.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
public class DashboardDTOResponse {

    private long totalOrders;
    private BigDecimal totalRevenue;
    private long todayOrders;
    private BigDecimal todayRevenue;
    private Map<OrderStatus, Long> ordersByStatus;
    private List<TopProductDTO> topProducts;

}