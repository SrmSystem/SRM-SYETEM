ALTER TABLE "PURCHASE_GOODS_REQUEST"
ADD ( "IS_MODIFY" NUMBER(1) DEFAULT 0  NULL  ) ;

COMMENT ON COLUMN "PURCHASE_GOODS_REQUEST"."IS_MODIFY" IS '修改标识（默认未修改 0）';



ALTER TABLE "QEWEB_PURCHASE_ORDER_ITEM"

ADD ( "SUR_BASE_QTY" NUMBER(11,3) DEFAULT 0.000  NULL  ) ;

COMMENT ON COLUMN "QEWEB_PURCHASE_ORDER_ITEM"."SUR_BASE_QTY" IS '订单剩余基本数量';


ALTER TABLE "QEWEB_PURCHASE_ORDER_ITEM_PLAN"
ADD ( "BASE_QTY" NUMBER(11,3) DEFAULT 0  NULL  ) ;

COMMENT ON COLUMN "QEWEB_PURCHASE_ORDER_ITEM_PLAN"."BASE_QTY" IS '匹配的基本数量';