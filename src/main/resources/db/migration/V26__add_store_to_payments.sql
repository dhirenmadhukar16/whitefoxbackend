ALTER TABLE payments
    ADD COLUMN store_id UUID;

UPDATE payments p
SET store_id = o.store_id
    FROM laundry_orders o
WHERE p.order_id = o.id;

ALTER TABLE payments
    ALTER COLUMN store_id SET NOT NULL;

ALTER TABLE payments
    ADD CONSTRAINT fk_payment_store
        FOREIGN KEY (store_id)
            REFERENCES stores(id);