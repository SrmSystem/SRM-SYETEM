alter table QEWEB_ORGANIZATION add audit_status NUMBER(1);
alter table QEWEB_ORGANIZATION add submit_status NUMBER(1);
alter table QEWEB_ORGANIZATION add audit_sn NUMBER(3);
-- Add comments to the columns 
comment on column QEWEB_ORGANIZATION.audit_status
  is '所有调查表的审核状态';
comment on column QEWEB_ORGANIZATION.submit_status
  is '所有调查表的提交状态';
comment on column QEWEB_ORGANIZATION.audit_sn
  is '审核顺序，已提交-已审核';