CREATE TABLE delivery_otps
(
    id UUID PRIMARY KEY,

    order_id UUID NOT NULL,

    otp VARCHAR(10) NOT NULL,

    verified BOOLEAN NOT NULL,

    expires_at TIMESTAMP,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_delivery_otp_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
);