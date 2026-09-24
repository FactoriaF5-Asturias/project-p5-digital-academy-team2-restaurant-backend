package factoriaf5.team2.goxu.billing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.billing.dtos.BillingReportDTOResponse;
import factoriaf5.team2.goxu.billing.dtos.InvoiceDTOResponse;
import factoriaf5.team2.goxu.dashboard.dtos.TopProductDTO;
import factoriaf5.team2.goxu.orders.OrderEntity;
import factoriaf5.team2.goxu.orders.OrderItemEntity;
import factoriaf5.team2.goxu.orders.OrderMapper;
import factoriaf5.team2.goxu.orders.OrderRepository;
import factoriaf5.team2.goxu.orders.OrderStatus;
import factoriaf5.team2.goxu.orders.dtos.OrderDTOResponse;
import factoriaf5.team2.goxu.products.ProductEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

    private static final int TOP_PRODUCTS_LIMIT = 5;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public BillingReportDTOResponse getReport(LocalDate startDate, LocalDate endDate) {
        List<OrderEntity> orders = orderRepository.findAll().stream()
                .filter(order -> isWithinRange(order, startDate, endDate))
                .toList();

        BigDecimal totalRevenue = sumTotals(orders);

        Map<LocalDate, BigDecimal> revenueByDay = orders.stream()
                .filter(order -> order.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, OrderEntity::getTotal, BigDecimal::add)));

        Map<OrderStatus, Long> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getStatus, Collectors.counting()));

        List<TopProductDTO> topProducts = topSellingProducts(orders);

        return BillingReportDTOResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalOrders(orders.size())
                .totalRevenue(totalRevenue)
                .revenueByDay(revenueByDay)
                .ordersByStatus(ordersByStatus)
                .topProducts(topProducts)
                .build();
    }

    @Transactional(readOnly = true)
    public InvoiceDTOResponse getInvoice(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado con id " + orderId));

        OrderDTOResponse orderResponse = orderMapper.toResponse(order);

        return InvoiceDTOResponse.builder()
                .orderId(orderResponse.getId())
                .userName(orderResponse.getUserName())
                .tableNumber(orderResponse.getTableNumber())
                .createdAt(orderResponse.getCreatedAt())
                .items(orderResponse.getItems())
                .total(orderResponse.getTotal())
                .build();
    }

    private boolean isWithinRange(OrderEntity order, LocalDate startDate, LocalDate endDate) {
        if (order.getCreatedAt() == null) {
            return false;
        }
        LocalDate orderDate = order.getCreatedAt().toLocalDate();
        return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
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