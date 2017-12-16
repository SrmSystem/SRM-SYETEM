alter table QEWEB_VENDOR_BASE_INFO add qs_certificate NVARCHAR2(500);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_BASE_INFO.qs_certificate
  is '质量体系证书文本';
  
-- Add/modify columns 
alter table QEWEB_MATERIAL_TYPE modify name NVARCHAR2(150);
alter table QEWEB_MATERIAL_TYPE add col1 NVARCHAR2(100);
-- Add comments to the columns 
comment on column QEWEB_MATERIAL_TYPE.col1
  is '科室';