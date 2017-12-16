alter table QEWEB_MATERIAL add rawmaterial_code NVARCHAR2(54);
alter table QEWEB_MATERIAL add box_type NVARCHAR2(54);
-- Add comments to the columns 
comment on column QEWEB_MATERIAL.rawmaterial_code
  is '材料牌号';
comment on column QEWEB_MATERIAL.box_type
  is '箱型';