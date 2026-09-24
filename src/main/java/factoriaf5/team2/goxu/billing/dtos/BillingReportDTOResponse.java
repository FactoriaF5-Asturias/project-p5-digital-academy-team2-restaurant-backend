package factoriaf5.team2.goxu.billing.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import factoriaf5.team2.goxu.dashboard.dtos.TopProductDTO;
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
public class BillingReportDTOResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private Map<LocalDate, BigDecimal> revenueByDay;
    private Map<OrderStatus, Long> ordersByStatus;
    private List<TopProductDTO> topProducts;

}