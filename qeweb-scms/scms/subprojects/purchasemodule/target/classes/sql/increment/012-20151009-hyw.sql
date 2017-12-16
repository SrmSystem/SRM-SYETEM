-- Add/modify columns 
alter table QEWEB_WMS_INVENTORY add REQ_MONTH_QTY NUMBER(11,2);
-- Add comments to the columns 
comment on column QEWEB_WMS_INVENTORY.REQ_MONTH_QTY is '当月需求量';