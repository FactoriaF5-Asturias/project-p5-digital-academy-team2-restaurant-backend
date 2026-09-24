package factoriaf5.team2.goxu.billing;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import factoriaf5.team2.goxu.billing.dtos.BillingReportDTOResponse;
import factoriaf5.team2.goxu.dashboard.dtos.TopProductDTO;
import factoriaf5.team2.goxu.orders.OrderStatus;

class BillingReportPdfGeneratorTest {

    private final BillingReportPdfGenerator generator = new BillingReportPdfGenerator();

    @Test
    void generate_shouldReturnNonEmptyPdfBytes() {
        BillingReportDTOResponse report = BillingReportDTOResponse.builder()
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 30))
                .totalOrders(2)
                .totalRevenue(new BigDecimal("47.00"))
                .revenueByDay(Map.of(LocalDate.of(2026, 9, 10), new BigDecimal("47.00")))
                .ordersByStatus(Map.of(OrderStatus.DELIVERED, 2L))
                .topProducts(List.of(
                        TopProductDTO.builder()
                                .productId(1L)
                                .productName("Fabada Asturiana")
                                .unitsSold(3)
                                .build()))
                .build();

        byte[] pdfBytes = generator.generate(report);

        assertThat(pdfBytes).isNotEmpty();
        assertThat(new String(pdfBytes, 0, 4)).isEqualTo("%PDF");
    }

}