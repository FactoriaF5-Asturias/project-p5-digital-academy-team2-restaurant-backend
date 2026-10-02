package factoriaf5.team2.goxu.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import factoriaf5.team2.goxu.dashboard.dtos.DashboardDTOResponse;
import factoriaf5.team2.goxu.orders.OrderEntity;
import factoriaf5.team2.goxu.orders.OrderItemEntity;
import factoriaf5.team2.goxu.orders.OrderRepository;
import factoriaf5.team2.goxu.orders.OrderStatus;
import factoriaf5.team2.goxu.products.ProductEntity;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getSummary_shouldAggregateTotalsStatusesAndTopProducts() {
        ProductEntity fabada = ProductEntity.builder()
                .id(1L)
                .name("Fabada Asturiana")
                .price(new BigDecimal("14.50"))
                .build();

        ProductEntity cachopo = ProductEntity.builder()
                .id(2L)
                .name("Cachopo")
                .price(new BigDecimal("18.00"))
                .build();

        OrderItemEntity item1 = OrderItemEntity.builder()
                .product(fabada)
                .quantity(3)
                .unitPrice(fabada.getPrice())
                .build();

        OrderItemEntity item2 = OrderItemEntity.builder()
                .product(cachopo)
                .quantity(1)
                .unitPrice(cachopo.getPrice())
                .build();

        OrderEntity todayOrder = OrderEntity.builder()
                .id(1L)
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("43.50"))
                .createdAt(LocalDateTime.now())
                .items(List.of(item1))
                .build();

        OrderEntity oldOrder = OrderEntity.builder()
                .id(2L)
                .status(OrderStatus.PENDING)
                .total(new BigDecimal("18.00"))
                .createdAt(LocalDateTime.now().minusDays(3))
                .items(List.of(item2))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(todayOrder, oldOrder));

        DashboardDTOResponse result = dashboardService.getSummary();

        assertThat(result.getTotalOrders()).isEqualTo(2);
        assertThat(result.getTotalRevenue()).isEqualTo(new BigDecimal("61.50"));
        assertThat(result.getTodayOrders()).isEqualTo(1);
        assertThat(result.getTodayRevenue()).isEqualTo(new BigDecimal("43.50"));
        assertThat(result.getOrdersByStatus().get(OrderStatus.DELIVERED)).isEqualTo(1);
        assertThat(result.getOrdersByStatus().get(OrderStatus.PENDING)).isEqualTo(1);
        assertThat(result.getTopProducts()).hasSize(2);
        assertThat(result.getTopProducts().get(0).getProductName()).isEqualTo("Fabada Asturiana");
        assertThat(result.getTopProducts().get(0).getUnitsSold()).isEqualTo(3);
    }

    @Test
    void getSummary_shouldReturnZeroValues_whenNoOrders() {
        when(orderRepository.findAll()).thenReturn(List.of());

        DashboardDTOResponse result = dashboardService.getSummary();

        assertThat(result.getTotalOrders()).isEqualTo(0);
        assertThat(result.getTotalRevenue()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getTopProducts()).isEmpty();
    }

}