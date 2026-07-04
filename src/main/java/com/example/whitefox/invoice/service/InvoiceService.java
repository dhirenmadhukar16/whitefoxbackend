package com.example.whitefox.invoice.service;
import com.example.whitefox.invoice.dto.InvoiceResponse;
import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse generateInvoice(UUID orderId);

    InvoiceResponse getInvoice(UUID invoiceId);

    InvoiceResponse getInvoiceByOrder(UUID orderId);
}
