-- QEWEB_PURCHASE_ORDER
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (ORDER_STATUS NUMBER(1)); 
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (TCOL1 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (TCOL2 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (TCOL3 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (TCOL4 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER ADD (TCOL5 DATE);

ALTER TABLE QEWEB_PURCHASE_ORDER ADD (VERSION NVARCHAR2(32));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER.VERSION IS '版本';


-- QEWEB_PURCHASE_ORDER_ITEM
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (ORDER_STATUS NUMBER(1)); 
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (ONWAY_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (RECEIVE_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (UNDELIVERY_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (RETURN_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DIFF_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (TCOL1 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (TCOL2 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (TCOL3 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (TCOL4 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (TCOL5 DATE);

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DCOL1 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DCOL2 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DCOL3 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DCOL4 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (DCOL5 NUMBER(11,2));

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (VERSION NVARCHAR2(32));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.VERSION IS '版本';

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (PUBLISH_STATUS NUMBER(1));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.PUBLISH_STATUS IS '发布状态';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (PUBLISH_USER_ID NUMBER(11));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.PUBLISH_USER_ID IS '发布人';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (PUBLISH_TIME DATE);
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.PUBLISH_TIME IS '发布时间';

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (VERIFY_STATUS NUMBER(1));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.VERIFY_STATUS IS '审核状态';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (VERIFY_USER_ID NUMBER(11));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.VERIFY_USER_ID IS '审核人';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (VERIFY_TIME DATE);
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.VERIFY_TIME IS '审核时间';

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (CLOSE_STATUS NUMBER(1));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.CLOSE_STATUS IS '关闭状态';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (CLOSE_USER_ID NUMBER(11));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.CLOSE_USER_ID IS '关闭人';
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM ADD (CLOSE_TIME DATE);
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM.CLOSE_TIME IS '关闭时间';

-- QEWEB_PURCHASE_ORDER_ITEM_PLAN
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (ORDER_STATUS NUMBER(1)); 
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (TCOL1 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (TCOL2 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (TCOL3 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (TCOL4 DATE);
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (TCOL5 DATE);

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (DCOL1 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (DCOL2 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (DCOL3 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (DCOL4 NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (DCOL5 NUMBER(11,2));

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (VERSION NVARCHAR2(32));
COMMENT ON COLUMN QEWEB_PURCHASE_ORDER_ITEM_PLAN.VERSION IS '版本';

ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (ONWAY_QTY NUMBER(11,2));
ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN ADD (UNDELIVERY_QTY NUMBER(11,2));
