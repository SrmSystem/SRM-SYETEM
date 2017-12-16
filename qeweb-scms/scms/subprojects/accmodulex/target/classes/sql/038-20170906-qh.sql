--预警添加
ALTER TABLE "QEWEB_WARN_MAIN"
ADD ( "IS_WARNING" NUMBER(1) NULL  ) ;

COMMENT ON COLUMN "QEWEB_WARN_MAIN"."IS_WARNING" IS '是否开启预警提醒';


CREATE TABLE "QEWEB_USER_WARN_REL" (
"ID" NUMBER(11) NOT NULL ,
"USER_ID" NUMBER(11) NULL ,
"ROLE_ID" NUMBER(11) NOT NULL ,
"ROLE_USER_ID" NUMBER(11) NULL ,
PRIMARY KEY ("ID")
)
NOCOMPRESS
;

COMMENT ON COLUMN "QEWEB_USER_WARN_REL"."ID" IS '主键';

COMMENT ON COLUMN "QEWEB_USER_WARN_REL"."USER_ID" IS '用户id';

COMMENT ON COLUMN "QEWEB_USER_WARN_REL"."ROLE_ID" IS '角色id';

COMMENT ON COLUMN "QEWEB_USER_WARN_REL"."ROLE_USER_ID" IS '角色人物id';



CREATE TABLE "QEWEB_GROUP_CONFIG_REL" (
"ID" NUMBER(11) NOT NULL ,
"USER_ID" NUMBER(11) NULL ,
"GROUP_IDS" NVARCHAR2(2000) NULL ,
PRIMARY KEY ("ID")
)
NOCOMPRESS
;

COMMENT ON COLUMN "QEWEB_GROUP_CONFIG_REL"."ID" IS 'ID';

COMMENT ON COLUMN "QEWEB_GROUP_CONFIG_REL"."USER_ID" IS '用户id';

COMMENT ON COLUMN "QEWEB_GROUP_CONFIG_REL"."GROUP_IDS" IS '采购组ids';



delete from  QEWEB_WARN_MAIN;
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('2', 'PLAN-10120', '预测计划', '预测计划已确认提醒', '111', '2222', '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:57:42:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:57:45:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '1', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('3', 'PLAN-10130', '预测计划', '预测计划驳回待处理提醒', '11', null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:20:06:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:20:11:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('1', 'PLAN-10110', '预测计划', '预测计划待发布提醒', '预测计划待发布提醒', null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:17:15:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 

2017-09-08 10:17:24:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('4', 'PLAN-10140', '预测计划', '预测计划待确认提醒', '预测计划待确认', null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:21:09:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-

09-08 10:21:16:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('5', 'PLAN-10150', '预测计划', '预测计划拒绝驳回待确认提醒', '预测计划拒绝驳回待确认提醒', null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:22:14:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', 

TO_TIMESTAMP(' 2017-09-08 10:22:17:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('6', 'PLAN-10160', '预测计划', '预测计划同意驳回提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:23:13:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:23:18:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('7', 'ORDER-20110', '采购订单', '采购订单同步(修改)提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:24:29:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:24:33:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('8', 'ORDER-20120', '采购订单', '采购订单已确认提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:25:46:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:25:51:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('9', 'ORDER-20130', '采购订单', '采购订单驳回待处理提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:26:42:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:26:46:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('10', 'ORDER-20140', '采购订单', '采购订单待确认提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:27:35:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:27:39:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('11', 'ORDER-20150', '采购订单', '采购订单拒绝驳回待确认提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:28:37:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-

08 10:28:40:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('12', 'ORDER-20160', '采购订单', '采购订单同意驳回提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:29:52:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:29:56:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('13', 'GOODS-30110', '要货计划', '要货计划待发布提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:30:56:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:30:59:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('14', 'GOODS-30120', '要货计划', '要货计划满足提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:32:01:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:32:05:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('15', 'GOODS-30130', '要货计划', '要货计划不满足提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:33:01:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:33:05:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('16', 'GOODS-30140', '要货计划', '要货计划待确认提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:33:54:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:34:05:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('17', 'ASN-40110', '采购收发货', 'ASN待审批提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:35:16:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:35:22:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '1');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('18', 'ASN-40120', '采购收发货', '已审批ASN修改数量提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:36:15:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:36:19:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('19', 'DN-40130', '采购收发货', '订单收货数据更新提醒', null, null, '12', '0', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:37:15:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:37:21:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('20', 'ASN-40140', '采购收发货', 'ASN审批确认提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:38:19:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:38:23:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');
INSERT INTO "QEWEB_WARN_MAIN" VALUES ('21', 'DN-40150', '采购收发货', '订单收货数据更新提醒', null, null, '12', '1', '1', '0', TO_TIMESTAMP(' 2017-09-08 10:39:53:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', TO_TIMESTAMP(' 2017-09-08 

10:39:55:000000', 'YYYY-MM-DD HH24:MI:SS:FF6'), '1', '超级管理员', '超级管理员', '0', null, null, null, null, '0');






