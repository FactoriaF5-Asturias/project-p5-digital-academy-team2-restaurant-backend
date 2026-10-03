package factoriaf5.team2.goxu.billing;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import factoriaf5.team2.goxu.billing.dtos.BillingReportDTOResponse;
import factoriaf5.team2.goxu.billing.dtos.BillingReportFileDTOResponse;
import factoriaf5.team2.goxu.billing.dtos.InvoiceDTOResponse;

@RestController
@RequestMapping(path = "${api-endpoint}/billing")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @GetMapping("/report")
    public ResponseEntity<BillingReportDTOResponse> getReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(service.getReport(startDate, endDate));
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<BillingReportFileDTOResponse> getReportPdf(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(service.generateReportPdf(startDate, endDate));
    }

    @GetMapping("/invoice/{orderId}")
    public ResponseEntity<InvoiceDTOResponse> getInvoice(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getInvoice(orderId));
    }

}