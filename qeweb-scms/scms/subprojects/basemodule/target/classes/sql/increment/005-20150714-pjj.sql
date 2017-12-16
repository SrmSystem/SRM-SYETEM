alter table QEWEB_MATERIAL_TYPE modify name NVARCHAR2(150);

alter table QEWEB_MATERIAL add parts_type NVARCHAR2(10);
-- Add comments to the columns 
comment on column QEWEB_MATERIAL.parts_type
  is '零部件类别';
  
alter table QEWEB_MATERIAL modify name NVARCHAR2(150);
alter table QEWEB_MATERIAL modify remark NVARCHAR2(255);
alter table QEWEB_MATERIAL modify pic_status NVARCHAR2(20);