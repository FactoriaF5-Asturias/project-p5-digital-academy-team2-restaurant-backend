package factoriaf5.team2.goxu.billing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.billing.dtos.BillingReportDTOResponse;
import factoriaf5.team2.goxu.dashboard.dtos.TopProductDTO;
import factoriaf5.team2.goxu.orders.OrderStatus;

@Component
public class BillingReportPdfGenerator {

    private static final float MARGIN_LEFT = 50;
    private static final float LINE_HEIGHT = 18;

    public byte[] generate(BillingReportDTOResponse report) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                float y = page.getMediaBox().getHeight() - 50;

                y = writeLine(stream, titleFont, 16, y, "Resumen de ventas");
                y -= 10;
                y = writeLine(stream, textFont, 11,
                        y, "Periodo: " + report.getStartDate() + " a " + report.getEndDate());
                y = writeLine(stream, textFont, 11, y, "Total de pedidos: " + report.getTotalOrders());
                y = writeLine(stream, textFont, 11,
                        y, "Ingresos totales: " + report.getTotalRevenue() + " EUR");

                y -= 10;
                y = writeLine(stream, titleFont, 12, y, "Pedidos por estado");
                for (Map.Entry<OrderStatus, Long> entry : report.getOrdersByStatus().entrySet()) {
                    y = writeLine(stream, textFont, 10, y, "- " + entry.getKey() + ": " + entry.getValue());
                }

                y -= 10;
                y = writeLine(stream, titleFont, 12, y, "Top productos");
                for (TopProductDTO product : report.getTopProducts()) {
                    y = writeLine(stream, textFont, 10,
                            y, "- " + product.getProductName() + ": " + product.getUnitsSold() + " unidades");
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el PDF del resumen de ventas", e);
        }
    }

    private float writeLine(PDPageContentStream stream, PDFont font, int fontSize, float y, String text)
            throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(MARGIN_LEFT, y);
        stream.showText(text);
        stream.endText();
        return y - LINE_HEIGHT;
    }

}