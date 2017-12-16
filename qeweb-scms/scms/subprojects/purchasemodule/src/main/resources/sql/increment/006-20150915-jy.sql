alter table qeweb_delivery add logistics_id number(11);

ALTER TABLE QEWEB_DELIVERY ADD (STATUS NUMBER(1) DEFAULT 0);
COMMENT ON COLUMN QEWEB_DELIVERY.STATUS IS '发货单状态0：正常 1：失效';

alter table qeweb_delivery add col1 nvarchar2(250);
alter table qeweb_delivery add col2 nvarchar2(100);
alter table qeweb_delivery add col3 nvarchar2(250);
alter table qeweb_delivery add col4 nvarchar2(100);
alter table qeweb_delivery add col5 nvarchar2(100);
alter table qeweb_delivery add col6 nvarchar2(100);
alter table qeweb_delivery add col7 nvarchar2(100);
alter table qeweb_delivery add col8 nvarchar2(100);
alter table qeweb_delivery add col9 nvarchar2(100);
alter table qeweb_delivery add col10 nvarchar2(100);


alter table qeweb_delivery_item add col1 nvarchar2(100);
alter table qeweb_delivery_item add col2 nvarchar2(100);
alter table qeweb_delivery_item add col3 nvarchar2(100);
alter table qeweb_delivery_item add col4 nvarchar2(100);
alter table qeweb_delivery_item add col5 nvarchar2(100);
alter table qeweb_delivery_item add col6 nvarchar2(100);
alter table qeweb_delivery_item add col7 nvarchar2(100);
alter table qeweb_delivery_item add col8 nvarchar2(100);
alter table qeweb_delivery_item add col9 nvarchar2(100);
alter table qeweb_delivery_item add col10 nvarchar2(100);