-- Create table
create table QEWEB_BACKLOG_CFG
(
  id               NUMBER(9) not null,
  content          NVARCHAR2(200),
  count            NUMBER(9),
  view_id          NUMBER(9),
  view_url         NVARCHAR2(200),
  org_role_type    NUMBER(1),
  right_user_ids   NVARCHAR2(500),
  query_hql        NVARCHAR2(300),
  query_sql        NVARCHAR2(300),
  params           NVARCHAR2(200),
  scriptlet        NVARCHAR2(1000),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  view_name        NVARCHAR2(200)
);
-- Add comments to the columns 
comment on column QEWEB_BACKLOG_CFG.id
  is '主键';
comment on column QEWEB_BACKLOG_CFG.content
  is '待办内容';
comment on column QEWEB_BACKLOG_CFG.count
  is '待办数量';
comment on column QEWEB_BACKLOG_CFG.view_id
  is '菜单ID';
comment on column QEWEB_BACKLOG_CFG.view_url
  is '菜单链接';
comment on column QEWEB_BACKLOG_CFG.org_role_type
  is '组织类型';
comment on column QEWEB_BACKLOG_CFG.right_user_ids
  is '指定用户';
comment on column QEWEB_BACKLOG_CFG.query_hql
  is '查询语句-hql';
comment on column QEWEB_BACKLOG_CFG.query_sql
  is '查询语句-sql';
comment on column QEWEB_BACKLOG_CFG.params
  is '参数列表';
comment on column QEWEB_BACKLOG_CFG.scriptlet
  is '脚本片段';
comment on column QEWEB_BACKLOG_CFG.remark
  is '备注';
comment on column QEWEB_BACKLOG_CFG.abolished
  is '废除标记';
comment on column QEWEB_BACKLOG_CFG.create_time
  is '创建时间';
comment on column QEWEB_BACKLOG_CFG.create_user_id
  is '创建用户FK';
comment on column QEWEB_BACKLOG_CFG.last_update_time
  is '最后更新时间';
comment on column QEWEB_BACKLOG_CFG.update_user_id
  is '最后更新用户FK';
comment on column QEWEB_BACKLOG_CFG.create_user_name
  is '创建用户姓名';
comment on column QEWEB_BACKLOG_CFG.update_user_name
  is '更新用户姓名';
comment on column QEWEB_BACKLOG_CFG.view_name
  is '菜单名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QEWEB_BACKLOG_CFG
  add constraint QEWEB_BACKLOG_CFG_PK primary key (ID);
  
INSERT INTO QEWEB_VIEW T (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
  VALUES (201044, 'backlog', '待办管理', NULL, 'manager/backlog/', 0, 1, 0, 5, NULL, 0, TO_TIMESTAMP('2015/08/26 13:59:21.947000000', 'YYYY/MM/DD HH24:MI:SS.FF'), 1, TO_TIMESTAMP('2015/08/26 13:59:21.947000000', 'YYYY/MM/DD HH24:MI:SS.FF'), 1, 'Admin', 'Admin');
COMMIT;