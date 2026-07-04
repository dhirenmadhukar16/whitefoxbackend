package com.example.whitefox.invoice.service;

import com.example.whitefox.invoice.dto.InvoiceResponse;
import com.example.whitefox.invoice.entity.Invoice;
import com.example.whitefox.invoice.repository.InvoiceRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final LaundryOrderRepository orderRepository;

    @Override
    public InvoiceResponse generateInvoice(UUID orderId) {

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        invoiceRepository.findByOrderId(orderId)
                .ifPresent(invoice -> {
                    throw new RuntimeException("Invoice already generated");
                });

        Invoice invoice = Invoice.builder()
                .order(order)
                .subtotal(order.getSubtotal())
                .gst(order.getGst())
                .totalAmount(order.getTotalAmount())
                .build();

        return map(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponse getInvoice(UUID invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return map(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceByOrder(UUID orderId) {

        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return map(invoice);
    }

    private InvoiceResponse map(Invoice invoice) {

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .orderId(invoice.getOrder().getId())
                .orderNumber(invoice.getOrder().getOrderNumber())
                .customerName(invoice.getOrder().getCustomer().getName())
                .subtotal(invoice.getSubtotal())
                .gst(invoice.getGst())
                .totalAmount(invoice.getTotalAmount())
                .generatedAt(invoice.getGeneratedAt())
                .build();
    }
}
