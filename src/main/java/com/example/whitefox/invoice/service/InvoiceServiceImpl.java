package com.example.whitefox.invoice.service;

import com.example.whitefox.invoice.dto.InvoiceResponse;
import com.example.whitefox.invoice.entity.Invoice;
import com.example.whitefox.invoice.repository.InvoiceRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;

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
    @Override
    public byte[] generateInvoicePdf(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        try {
            String html = buildInvoiceHtml(invoice);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF: " + e.getMessage());
        }
    }
    private String buildInvoiceHtml(Invoice invoice) {
        LaundryOrder order = invoice.getOrder();

        String customerName = order.getCustomer() != null
                ? order.getCustomer().getName()
                : "-";

        String storeName = order.getStore() != null
                ? order.getStore().getName()
                : "-";

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        color: #222;
                        padding: 30px;
                    }
                    .header {
                        display: flex;
                        justify-content: space-between;
                        border-bottom: 3px solid #1b5e20;
                        padding-bottom: 16px;
                        margin-bottom: 24px;
                    }
                    .brand {
                        font-size: 28px;
                        font-weight: bold;
                        color: #1b5e20;
                    }
                    .small {
                        color: #666;
                        font-size: 12px;
                    }
                    .title {
                        text-align: right;
                        font-size: 22px;
                        font-weight: bold;
                    }
                    .section {
                        margin-top: 20px;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-top: 12px;
                    }
                    th {
                        background: #e8f5e9;
                        text-align: left;
                        padding: 10px;
                        border: 1px solid #ddd;
                    }
                    td {
                        padding: 10px;
                        border: 1px solid #ddd;
                    }
                    .right {
                        text-align: right;
                    }
                    .total {
                        font-size: 18px;
                        font-weight: bold;
                        color: #1b5e20;
                    }
                    .footer {
                        margin-top: 40px;
                        font-size: 12px;
                        color: #777;
                        border-top: 1px solid #ddd;
                        padding-top: 12px;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <div>
                        <div class="brand">WhiteFox Laundry</div>
                        <div class="small">Professional Laundry & Dry Cleaning Services</div>
                    </div>
                    <div class="title">
                        GST INVOICE<br/>
                        <span class="small">%s</span>
                    </div>
                </div>

                <div class="section">
                    <table>
                        <tr>
                            <td><b>Invoice No</b></td>
                            <td>%s</td>
                            <td><b>Invoice Date</b></td>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <td><b>Order No</b></td>
                            <td>%s</td>
                            <td><b>Store</b></td>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <td><b>Customer</b></td>
                            <td colspan="3">%s</td>
                        </tr>
                    </table>
                </div>

                <div class="section">
                    <table>
                        <thead>
                            <tr>
                                <th>Description</th>
                                <th class="right">Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Laundry / Dry Cleaning Services</td>
                                <td class="right">₹ %.2f</td>
                            </tr>
                            <tr>
                                <td>GST</td>
                                <td class="right">₹ %.2f</td>
                            </tr>
                            <tr>
                                <td class="total">Total Amount</td>
                                <td class="right total">₹ %.2f</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="footer">
                    Thank you for choosing WhiteFox Laundry.<br/>
                    This is a computer-generated invoice.
                </div>
            </body>
            </html>
            """.formatted(
                invoice.getInvoiceNumber(),
                invoice.getInvoiceNumber(),
                invoice.getGeneratedAt(),
                order.getOrderNumber(),
                storeName,
                customerName,
                invoice.getSubtotal() != null ? invoice.getSubtotal() : 0.0,
                invoice.getGst() != null ? invoice.getGst() : 0.0,
                invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0.0        );
    }
}
