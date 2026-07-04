package com.example.whitefox.invoice.controller;

import com.example.whitefox.invoice.dto.InvoiceResponse;
import com.example.whitefox.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/orders/{orderId}/generate")
    public InvoiceResponse generateInvoice(
            @PathVariable UUID orderId
    ) {
        return invoiceService.generateInvoice(orderId);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponse getInvoice(
            @PathVariable UUID invoiceId
    ) {
        return invoiceService.getInvoice(invoiceId);
    }

    @GetMapping("/orders/{orderId}")
    public InvoiceResponse getInvoiceByOrder(
            @PathVariable UUID orderId
    ) {
        return invoiceService.getInvoiceByOrder(orderId);
    }
    @GetMapping("/{invoiceId}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(
            @PathVariable UUID invoiceId
    ) {
        byte[] pdf = invoiceService.generateInvoicePdf(invoiceId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
