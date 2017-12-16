-- Add/modify columns 
alter table QEWEB_VENDOR_MATTYPE_REL add TOP_MATERIAL_TYPE_NAME NVARCHAR2(255);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_MATTYPE_REL.TOP_MATERIAL_TYPE_NAME
  is '最顶层物料类型名称';
