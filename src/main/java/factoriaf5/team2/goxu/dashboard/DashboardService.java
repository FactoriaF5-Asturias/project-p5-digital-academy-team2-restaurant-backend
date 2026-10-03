package factoriaf5.team2.goxu.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import factoriaf5.team2.goxu.dashboard.dtos.DashboardDTOResponse;
import factoriaf5.team2.goxu.dashboard.dtos.TopProductDTO;
import factoriaf5.team2.goxu.orders.OrderEntity;
import factoriaf5.team2.goxu.orders.OrderItemEntity;
import factoriaf5.team2.goxu.orders.OrderRepository;
import factoriaf5.team2.goxu.orders.OrderStatus;
import factoriaf5.team2.goxu.products.ProductEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int TOP_PRODUCTS_LIMIT = 5;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public DashboardDTOResponse getSummary() {
        List<OrderEntity> orders = orderRepository.findAll();
        LocalDate today = LocalDate.now();

        List<OrderEntity> todayOrdersList = orders.stream()
                .filter(order -> order.getCreatedAt() != null
                        && order.getCreatedAt().toLocalDate().isEqual(today))
                .toList();

        BigDecimal totalRevenue = sumTotals(orders);
        BigDecimal todayRevenue = sumTotals(todayOrdersList);

        Map<OrderStatus, Long> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getStatus, Collectors.counting()));

        List<TopProductDTO> topProducts = topSellingProducts(orders);

        return DashboardDTOResponse.builder()
                .totalOrders(orders.size())
                .totalRevenue(totalRevenue)
                .todayOrders(todayOrdersList.size())
                .todayRevenue(todayRevenue)
                .ordersByStatus(ordersByStatus)
                .topProducts(topProducts)
                .build();
    }

    private BigDecimal sumTotals(List<OrderEntity> orders) {
        return orders.stream()
                .map(OrderEntity::getTotal)
                .filter(total -> total != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TopProductDTO> topSellingProducts(List<OrderEntity> orders) {
        List<OrderItemEntity> items = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .toList();

        Map<Long, Long> unitsByProductId = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingLong(OrderItemEntity::getQuantity)));

        Map<Long, String> productNamesById = items.stream()
                .map(OrderItemEntity::getProduct)
                .collect(Collectors.toMap(
                        ProductEntity::getId,
                        ProductEntity::getName,
                        (existingName, newName) -> existingName));

        return unitsByProductId.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(TOP_PRODUCTS_LIMIT)
                .map(entry -> TopProductDTO.builder()
                        .productId(entry.getKey())
                        .productName(productNamesById.get(entry.getKey()))
                        .unitsSold(entry.getValue())
                        .build())
                .toList();
    }

}