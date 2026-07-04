CREATE TABLE IF NOT EXISTS invoices (
                                        id UUID PRIMARY KEY,
                                        invoice_number VARCHAR(100) UNIQUE NOT NULL,
    order_id UUID NOT NULL UNIQUE,
    subtotal DOUBLE PRECISION,
    gst DOUBLE PRECISION,
    total_amount DOUBLE PRECISION,
    paid_amount DOUBLE PRECISION,
    remaining_amount DOUBLE PRECISION,
    status VARCHAR(50),
    generated_at TIMESTAMP,
    CONSTRAINT fk_invoice_order
    FOREIGN KEY (order_id)
    REFERENCES laundry_orders(id)
    );