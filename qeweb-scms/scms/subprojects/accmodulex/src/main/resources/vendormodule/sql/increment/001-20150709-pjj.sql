--Create table
create table QEWEB_VENDOR_CHANGEHIS
(
  id               NUMBER(9) not null,
  org_id           NUMBER(9) not null,
  vendor_name      NVARCHAR2(100),
  change_type      NUMBER(1),
  change_type_text NVARCHAR2(50),
  change_user      NVARCHAR2(50),
  change_time      TIMESTAMP(6),
  change_reason    NVARCHAR2(255)
);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_CHANGEHIS.id
  is '主键';
comment on column QEWEB_VENDOR_CHANGEHIS.org_id
  is '被变更的组织';
comment on column QEWEB_VENDOR_CHANGEHIS.vendor_name
  is '被变更的供应商名称';
comment on column QEWEB_VENDOR_CHANGEHIS.change_type
  is '变更类型';
comment on column QEWEB_VENDOR_CHANGEHIS.change_type_text
  is '变更类型的文本';
comment on column QEWEB_VENDOR_CHANGEHIS.change_user
  is '变更的人';
comment on column QEWEB_VENDOR_CHANGEHIS.change_time
  is '变更时间';
comment on column QEWEB_VENDOR_CHANGEHIS.change_reason
  is '变更原因';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QEWEB_VENDOR_CHANGEHIS
  add constraint QEWEB_VENDOR_CHANGEHIS_PK primary key (ID);