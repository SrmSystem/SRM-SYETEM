drop table QEWEB_MATERIAL_SUPPLY_REL;
-- Create table
create table QEWEB_VENDOR_MAT_SUPPLY_REL
(
  id                   NUMBER(9) not null,
  material_rel_id      NUMBER(9),
  vendor_id            NUMBER(9),
  vendor_name          NVARCHAR2(255),
  material_id          NUMBER(9),
  material_name        NVARCHAR2(255),
  bussiness_range_id   NUMBER(9),
  bussiness_range_name NVARCHAR2(255),
  bussiness_id         NUMBER(9),
  bussiness_name       NVARCHAR2(255),
  brand_id             NUMBER(9),
  brand_name           NVARCHAR2(255),
  product_line_id      NUMBER(9),
  product_line_name    NVARCHAR2(255),
  factory_id           NUMBER(9),
  factory_name         NVARCHAR2(255),
  ismain               NUMBER(1),
  supply_coefficient   NUMBER(20),
  abolished            NUMBER(1),
  create_time          TIMESTAMP(6),
  create_user_id       NUMBER(9),
  last_update_time     TIMESTAMP(6),
  update_user_id       NUMBER(9),
  create_user_name     NVARCHAR2(100),
  update_user_name     NVARCHAR2(100),
  leaf                 NUMBER(1) default 0,
  importance           NUMBER(1) default 0,
  need_second_vendor   NUMBER(1) default 0,
  org_id               NUMBER(9) not null
);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.abolished
  is '�ϳ����';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.create_time
  is '����ʱ��';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.create_user_id
  is '�����û�FK';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.last_update_time
  is '������ʱ��';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.update_user_id
  is '�������û�FK';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.create_user_name
  is '�����û�����';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.update_user_name
  is '�����û�����';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.leaf
  is '�Ƿ�ΪҶ�ӽڵ�';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.importance
  is '��Ҫ��';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.need_second_vendor
  is '�Ƿ���Ҫ������Ӧ��';
comment on column QEWEB_VENDOR_MAT_SUPPLY_REL.org_id
  is '��֯ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QEWEB_VENDOR_MAT_SUPPLY_REL
  add primary key (ID);


-- Create table
drop table QEWEB_MATERIAL_REL;
create table QEWEB_VENDOR_MATERIAL_REL
(
  id                 NUMBER(9) not null,
  org_id             NUMBER(9) not null,
  vendor_id          NUMBER(9),
  vendor_name        NVARCHAR2(255),
  material_id        NUMBER(9),
  material_name      NVARCHAR2(255),
  status             NUMBER(1),
  data_from          NVARCHAR2(255),
  abolished          NUMBER(1),
  create_time        TIMESTAMP(6),
  create_user_id     NUMBER(9),
  last_update_time   TIMESTAMP(6),
  update_user_id     NUMBER(9),
  create_user_name   NVARCHAR2(100),
  update_user_name   NVARCHAR2(100),
  leaf               NUMBER(1) default 0,
  importance         NUMBER(1) default 0,
  need_second_vendor NUMBER(1) default 0,
  car_model          NVARCHAR2(255)
);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_MATERIAL_REL.id
  is '����';
comment on column QEWEB_VENDOR_MATERIAL_REL.org_id
  is '��֯ID';
comment on column QEWEB_VENDOR_MATERIAL_REL.vendor_id
  is '��Ӧ��ID';
comment on column QEWEB_VENDOR_MATERIAL_REL.material_id
  is '����ID';
comment on column QEWEB_VENDOR_MATERIAL_REL.abolished
  is '�ϳ����';
comment on column QEWEB_VENDOR_MATERIAL_REL.create_time
  is '����ʱ��';
comment on column QEWEB_VENDOR_MATERIAL_REL.create_user_id
  is '�����û�FK';
comment on column QEWEB_VENDOR_MATERIAL_REL.last_update_time
  is '������ʱ��';
comment on column QEWEB_VENDOR_MATERIAL_REL.update_user_id
  is '�������û�FK';
comment on column QEWEB_VENDOR_MATERIAL_REL.create_user_name
  is '�����û�����';
comment on column QEWEB_VENDOR_MATERIAL_REL.update_user_name
  is '�����û�����';
comment on column QEWEB_VENDOR_MATERIAL_REL.leaf
  is '�Ƿ�ΪҶ�ӽڵ�';
comment on column QEWEB_VENDOR_MATERIAL_REL.importance
  is '��Ҫ��';
comment on column QEWEB_VENDOR_MATERIAL_REL.need_second_vendor
  is '�Ƿ���Ҫ������Ӧ��';
comment on column QEWEB_VENDOR_MATERIAL_REL.car_model
  is '��˳���';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QEWEB_VENDOR_MATERIAL_REL
  add primary key (ID);