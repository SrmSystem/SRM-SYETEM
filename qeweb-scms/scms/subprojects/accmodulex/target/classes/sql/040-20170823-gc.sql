--补货
ALTER TABLE qeweb_delivery
 ADD (SHIP_TYPE number(1));

COMMENT ON COLUMN "qeweb_delivery"."SHIP_TYPE" IS '发货类型1普通的发货 -1补货';



ALTER TABLE qeweb_purchase_order_item_plan
 ADD (SHIP_TYPE number(1));

COMMENT ON COLUMN "qeweb_purchase_order_item_plan"."SHIP_TYPE" IS '发货类型1普通的发货 -1补货';


ALTER TABLE qeweb_purchase_order_item_plan
 ADD (REC_ITEM_ID number(11));

COMMENT ON COLUMN "qeweb_purchase_order_item_plan"."REC_ITEM_ID" IS '收货明细id';

ALTER TABLE qeweb_purchase_order_item_plan
 ADD （DN number(11));

COMMENT ON COLUMN "qeweb_purchase_order_item_plan"."DN" IS 'DN号';

ALTER TABLE qeweb_purchase_order_item_plan
 ADD （SOURCE_ORDER_ITEM_PLAN_ID  number(11));

COMMENT ON COLUMN "qeweb_purchase_order_item_plan"."SOURCE_ORDER_ITEM_PLAN_ID" IS '原供货计划，即普通发货单的供货计划';



ALTER TABLE qeweb_receive
 ADD (REPL_RECEIVE_STATUS number(1));

COMMENT ON COLUMN "qeweb_receive"."REPL_RECEIVE_STATUS" IS '补货的收货状态';


ALTER TABLE qeweb_receive
 ADD (REPL_DELIVERY_STATUS number(1));

COMMENT ON COLUMN "qeweb_receive"."REPL_DELIVERY_STATUS" IS '补货的发货状态';




ALTER TABLE qeweb_receive_item
 ADD (REPL_ONWAY_QTY number(11,3));

COMMENT ON COLUMN "qeweb_receive_item"."REPL_ONWAY_QTY" IS '补货的在途数量';

ALTER TABLE qeweb_receive_item
 ADD (REPL_RECEIVE_QTY number(11,3));

COMMENT ON COLUMN "qeweb_receive_item"."REPL_RECEIVE_QTY" IS '补货的收货数量';


ALTER TABLE qeweb_receive_item
 ADD (REPL_UNDELIVERY_QTY number(11,3));

COMMENT ON COLUMN "qeweb_receive_item"."REPL_UNDELIVERY_QTY" IS '补货未发数量';



ALTER TABLE qeweb_receive_item
 ADD (REPL_DELIVERY_STATUS number(11,3));

COMMENT ON COLUMN "qeweb_receive_item"."REPL_DELIVERY_STATUS" IS '补货发货状态';


update qeweb_delivery set SHIP_TYPE =1 where SHIP_TYPE  is null;

update qeweb_purchase_order_item_plan set  SHIP_TYPE =1  where SHIP_TYPE  is null;

