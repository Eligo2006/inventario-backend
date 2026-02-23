package pe.cibertec.inventario.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.inventario.entity.Producto;
import pe.cibertec.inventario.repository.ProductoRepository;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ProductoRepository productoRepository;

    public ReportesController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Reporte exportable en CSV (compatible con Excel).
     * Requisito del curso: "reportes exportables a diferentes formatos".
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(value = "/productos.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportarProductosCsv() {
        List<Producto> productos = productoRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("id,nombre,descripcion,precio,stock,estado\n");
        for (Producto p : productos) {
            sb.append(p.getId()).append(',')
              .append(escapeCsv(p.getNombre())).append(',')
              .append(escapeCsv(p.getDescripcion())).append(',')
              .append(p.getPrecio()).append(',')
              .append(p.getStock()).append(',')
              .append(p.getEstado()).append('\n');
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(bytes);
    }

    /**
     * Reporte exportable en PDF.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(value = "/productos.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportarProductosPdf() {
        List<Producto> productos = productoRepository.findAll();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("Reporte de Productos - Inventario", titleFont));
            document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 3.0f, 4.0f, 2.0f, 1.6f, 1.6f});

            addHeader(table, "ID");
            addHeader(table, "Nombre");
            addHeader(table, "Descripción");
            addHeader(table, "Precio");
            addHeader(table, "Stock");
            addHeader(table, "Estado");

            for (Producto p : productos) {
                table.addCell(String.valueOf(p.getId()));
                table.addCell(nullToEmpty(p.getNombre()));
                table.addCell(nullToEmpty(p.getDescripcion()));
                table.addCell(String.valueOf(p.getPrecio()));
                table.addCell(String.valueOf(p.getStock()));
                table.addCell(String.valueOf(p.getEstado()));
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(6);
        cell.setBackgroundColor(new java.awt.Color(230, 230, 230));
        table.addCell(cell);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String v = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
