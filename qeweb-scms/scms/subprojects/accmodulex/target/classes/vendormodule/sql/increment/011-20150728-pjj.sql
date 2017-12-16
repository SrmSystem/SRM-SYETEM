create table QEWEB_VENDOR_MATTYPE_REL
(
  id                 NUMBER(9) not null,
  org_id             NUMBER(9),
  org_code           NVARCHAR2(50),
  org_name           VARCHAR2(50),
  material_type_id   NUMBER(9),
  material_type_code VARCHAR2(50),
  material_type_name VARCHAR2(50),
  create_user_id     NUMBER(11),
  create_user_name   NVARCHAR2(255),
  create_time        DATE,
  update_user_id     NUMBER(11),
  update_user_name   NVARCHAR2(255),
  last_update_time   DATE,
  abolished          NUMBER(1) not null,
  vendor_id          NUMBER(9)
)

-- Add comments to the columns 
comment on column QEWEB_VENDOR_MATTYPE_REL.id
  is '主键';
comment on column QEWEB_VENDOR_MATTYPE_REL.org_id
  is '组织ID';
comment on column QEWEB_VENDOR_MATTYPE_REL.org_code
  is '组织编码';
comment on column QEWEB_VENDOR_MATTYPE_REL.org_name
  is '组织名称';
comment on column QEWEB_VENDOR_MATTYPE_REL.material_type_id
  is '物料分类ID';
comment on column QEWEB_VENDOR_MATTYPE_REL.material_type_code
  is '物料分类编码';
comment on column QEWEB_VENDOR_MATTYPE_REL.material_type_name
  is '物料分类名称';
comment on column QEWEB_VENDOR_MATTYPE_REL.create_user_id
  is '创建人ID';
comment on column QEWEB_VENDOR_MATTYPE_REL.create_user_name
  is '创建人名称';
comment on column QEWEB_VENDOR_MATTYPE_REL.create_time
  is '创建时间';
comment on column QEWEB_VENDOR_MATTYPE_REL.update_user_id
  is '修改人ID';
comment on column QEWEB_VENDOR_MATTYPE_REL.update_user_name
  is '修改人名称';
comment on column QEWEB_VENDOR_MATTYPE_REL.last_update_time
  is '修改时间';
comment on column QEWEB_VENDOR_MATTYPE_REL.abolished
  is '删除状态';
comment on column QEWEB_VENDOR_MATTYPE_REL.vendor_id
  is '供应商ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QEWEB_VENDOR_MATTYPE_REL
  add constraint QEWEB_VENDOR_MATTYPE_REL_PK primary key (ID);
