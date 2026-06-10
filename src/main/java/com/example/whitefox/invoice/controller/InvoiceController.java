package com.example.whitefox.invoice.controller;

import com.example.whitefox.invoice.dto.InvoiceResponse;
import com.example.whitefox.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
}
