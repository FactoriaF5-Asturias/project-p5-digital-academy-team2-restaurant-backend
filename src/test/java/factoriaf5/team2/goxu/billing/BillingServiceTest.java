package factoriaf5.team2.goxu.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.billing.dtos.BillingReportDTOResponse;
import factoriaf5.team2.goxu.billing.dtos.BillingReportFileDTOResponse;
import factoriaf5.team2.goxu.billing.dtos.InvoiceDTOResponse;
import factoriaf5.team2.goxu.orders.OrderEntity;
import factoriaf5.team2.goxu.orders.OrderItemEntity;
import factoriaf5.team2.goxu.orders.OrderMapper;
import factoriaf5.team2.goxu.orders.OrderRepository;
import factoriaf5.team2.goxu.orders.OrderStatus;
import factoriaf5.team2.goxu.orders.dtos.OrderDTOResponse;
import factoriaf5.team2.goxu.orders.dtos.OrderItemDTOResponse;
import factoriaf5.team2.goxu.products.ProductEntity;
import factoriaf5.team2.goxu.storage.CloudStorageService;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private BillingReportPdfGenerator pdfGenerator;

    @Mock
    private CloudStorageService cloudStorageService;

    @InjectMocks
    private BillingService billingService;

    @Test
    void getReport_shouldAggregateOnlyOrdersWithinDateRange() {
        ProductEntity fabada = ProductEntity.builder()
                .id(1L)
                .name("Fabada Asturiana")
                .price(new BigDecimal("14.50"))
                .build();

        OrderItemEntity item = OrderItemEntity.builder()
                .product(fabada)
                .quantity(2)
                .unitPrice(fabada.getPrice())
                .build();

        OrderEntity insideRange = OrderEntity.builder()
                .id(1L)
                .status(OrderStatus.DELIVERED)
                .paid(true)
                .total(new BigDecimal("29.00"))
                .createdAt(LocalDateTime.of(2026, 9, 10, 13, 0))
                .items(List.of(item))
                .build();

        OrderEntity outsideRange = OrderEntity.builder()
                .id(2L)
                .status(OrderStatus.DELIVERED)
                .paid(true)
                .total(new BigDecimal("18.00"))
                .createdAt(LocalDateTime.of(2026, 8, 1, 13, 0))
                .items(List.of())
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(insideRange, outsideRange));

        BillingReportDTOResponse result = billingService.getReport(
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertThat(result.getTotalOrders()).isEqualTo(1);
        assertThat(result.getTotalRevenue()).isEqualTo(new BigDecimal("29.00"));
        assertThat(result.getRevenueByDay().get(LocalDate.of(2026, 9, 10))).isEqualTo(new BigDecimal("29.00"));
        assertThat(result.getOrdersByStatus().get(OrderStatus.DELIVERED)).isEqualTo(1);
        assertThat(result.getTopProducts()).hasSize(1);
        assertThat(result.getTopProducts().get(0).getProductName()).isEqualTo("Fabada Asturiana");
    }

    @Test
    void getReport_shouldExcludeUnpaidOrders_evenWithinDateRange() {
        OrderEntity unpaidOrder = OrderEntity.builder()
                .id(3L)
                .status(OrderStatus.DELIVERED)
                .paid(false)
                .total(new BigDecimal("14.50"))
                .createdAt(LocalDateTime.of(2026, 9, 15, 13, 0))
                .items(List.of())
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(unpaidOrder));

        BillingReportDTOResponse result = billingService.getReport(
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertThat(result.getTotalOrders()).isEqualTo(0);
        assertThat(result.getTotalRevenue()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getReport_shouldReturnZeroValues_whenNoOrdersInRange() {
        when(orderRepository.findAll()).thenReturn(List.of());

        BillingReportDTOResponse result = billingService.getReport(
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertThat(result.getTotalOrders()).isEqualTo(0);
        assertThat(result.getTotalRevenue()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getTopProducts()).isEmpty();
    }

    @Test
    void generateReportPdf_shouldGeneratePdfAndUploadIt_returningFileInfo() {
        when(orderRepository.findAll()).thenReturn(List.of());

        byte[] fakePdf = "%PDF-fake".getBytes();
        when(pdfGenerator.generate(any(BillingReportDTOResponse.class))).thenReturn(fakePdf);
        when(cloudStorageService.uploadFile(any(String.class), eq(fakePdf), eq("application/pdf")))
                .thenReturn("https://storage.example.com/informe-ventas.pdf");

        BillingReportFileDTOResponse result = billingService.generateReportPdf(
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertThat(result.getUrl()).isEqualTo("https://storage.example.com/informe-ventas.pdf");
        assertThat(result.getFileName()).contains("2026-09-01").contains("2026-09-30");
        assertThat(result.getGeneratedAt()).isNotNull();
    }

    @Test
    void getInvoice_shouldReturnInvoice_whenOrderExists() {
        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .tableNumber("5")
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("29.00"))
                .createdAt(LocalDateTime.of(2026, 9, 10, 13, 0))
                .items(List.of())
                .build();

        OrderDTOResponse orderResponse = OrderDTOResponse.builder()
                .id(1L)
                .userId(1L)
                .userName("Cliente Test")
                .tableNumber("5")
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("29.00"))
                .createdAt(LocalDateTime.of(2026, 9, 10, 13, 0))
                .items(List.<OrderItemDTOResponse>of())
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        InvoiceDTOResponse result = billingService.getInvoice(1L);

        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("Cliente Test");
        assertThat(result.getTableNumber()).isEqualTo("5");
        assertThat(result.getTotal()).isEqualTo(new BigDecimal("29.00"));
    }

    @Test
    void getInvoice_shouldThrow_whenOrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billingService.getInvoice(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

}