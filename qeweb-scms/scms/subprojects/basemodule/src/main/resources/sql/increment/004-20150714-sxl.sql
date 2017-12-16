/*
Navicat Oracle Data Transfer
Oracle Client Version : 10.2.0.5.0

Source Server         : 福田
Source Server Version : 110200
Source Host           : 221.224.132.210:1521
Source Schema         : QEWEBREPOSITORIES

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2015-07-14 16:48:36
*/


-- ----------------------------
-- Table structure for QEWEB_NOTICE
-- ----------------------------
DROP TABLE "QEWEB_NOTICE";
CREATE TABLE "QEWEB_NOTICE" (
"ID" NUMBER(11) NOT NULL ,
"TITLE" NVARCHAR2(255) NULL ,
"CONTENT" CLOB NULL ,
"CREATE_TIME" DATE NULL ,
"CREATE_USER_ID" NUMBER(11) NULL ,
"LAST_UPDATE_TIME" DATE NULL ,
"UPDATE_USER_ID" NUMBER(11) NULL ,
"CREATE_USER_NAME" NVARCHAR2(255) NULL ,
"UPDATE_USER_NAME" NVARCHAR2(255) NULL ,
"ABOLISHED" NUMBER(1) NULL ,
"COMMENT_POWER" NUMBER(1) NULL ,
"VALID_START_TIME" DATE NOT NULL ,
"VALID_END_TIME" DATE NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "QEWEB_NOTICE"."TITLE" IS '标题';
COMMENT ON COLUMN "QEWEB_NOTICE"."CONTENT" IS '内容';
COMMENT ON COLUMN "QEWEB_NOTICE"."ABOLISHED" IS '废除标记';
COMMENT ON COLUMN "QEWEB_NOTICE"."COMMENT_POWER" IS '允许评论和不允许评论';

-- ----------------------------
-- Indexes structure for table QEWEB_NOTICE
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_NOTICE
-- ----------------------------
ALTER TABLE "QEWEB_NOTICE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "QEWEB_NOTICE" ADD CHECK ("VALID_START_TIME" IS NOT NULL);
ALTER TABLE "QEWEB_NOTICE" ADD CHECK ("VALID_END_TIME" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_NOTICE
-- ----------------------------
ALTER TABLE "QEWEB_NOTICE" ADD PRIMARY KEY ("ID");
