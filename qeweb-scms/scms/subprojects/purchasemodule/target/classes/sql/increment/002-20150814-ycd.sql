ALTER TABLE QEWEB_PURCHASE_ORDER ADD (order_type NUMBER(2));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER.order_type IS '订单类型';

ALTER TABLE QEWEB_DELIVERY ADD (order_id NUMBER(11));
COMMENT ON COLUMN QEWEB_DELIVERY.order_id IS '订单ID';