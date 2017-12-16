/**系统消息*/
DROP TABLE "QEWEB_MESSAGE" CASCADE CONSTRAINTS;
CREATE TABLE "QEWEB_MESSAGE" 
(
   "ID"                 NUMBER(11)           NOT NULL,
   "TITLE"              NVARCHAR2(64)        NOT NULL,
   "MSG"                NVARCHAR2(254),
   "MODULE_ID"          NUMBER(11),
   "FROM_USER_ID"       NUMBER(11),
   "TO_USER_ID"         NUMBER(11),
   "IS_READ"            NUMBER(1),
   "CREATE_USER_ID"     NUMBER(11),
   "CREATE_USER_NAME"   NVARCHAR2(255),
   "CREATE_TIME"        DATE,
   "UPDATE_USER_ID"     NUMBER(11),
   "UPDATE_USER_NAME"   NVARCHAR2(255),
   "LAST_UPDATE_TIME"   DATE,
   "ABOLISHED"          NUMBER(1)            NOT NULL,
   CONSTRAINT PK_QEWEB_MESSAGE PRIMARY KEY ("ID")
);

COMMENT ON TABLE "QEWEB_MESSAGE" IS '系统消息';

COMMENT ON COLUMN "QEWEB_MESSAGE"."ID" IS '主键';

COMMENT ON COLUMN "QEWEB_MESSAGE"."TITLE" IS '标题';

COMMENT ON COLUMN "QEWEB_MESSAGE"."MSG" IS '详情';

COMMENT ON COLUMN "QEWEB_MESSAGE"."MODULE_ID" IS '模块ID';

COMMENT ON COLUMN "QEWEB_MESSAGE"."FROM_USER_ID" IS '发送人';

COMMENT ON COLUMN "QEWEB_MESSAGE"."TO_USER_ID" IS '接收人';

COMMENT ON COLUMN "QEWEB_MESSAGE"."IS_READ" IS '已读状态';

COMMENT ON COLUMN "QEWEB_MESSAGE"."CREATE_USER_ID" IS '创建人ID';

COMMENT ON COLUMN "QEWEB_MESSAGE"."CREATE_USER_NAME" IS '创建人名称';

COMMENT ON COLUMN "QEWEB_MESSAGE"."CREATE_TIME" IS '创建时间';

COMMENT ON COLUMN "QEWEB_MESSAGE"."UPDATE_USER_ID" IS '修改人ID';

COMMENT ON COLUMN "QEWEB_MESSAGE"."UPDATE_USER_NAME" IS '修改人名称';

COMMENT ON COLUMN "QEWEB_MESSAGE"."LAST_UPDATE_TIME" IS '修改时间';

COMMENT ON COLUMN "QEWEB_MESSAGE"."ABOLISHED" IS '删除状态';
