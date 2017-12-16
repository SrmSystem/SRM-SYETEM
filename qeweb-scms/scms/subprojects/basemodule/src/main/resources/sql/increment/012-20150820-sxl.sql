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

Date: 2015-08-20 09:51:24
*/


-- ----------------------------
-- Table structure for QEWEB_NOTICE_LOOK
-- ----------------------------
DROP TABLE "QEWEB_NOTICE_LOOK";
CREATE TABLE "QEWEB_NOTICE_LOOK" (
"ID" NUMBER(9) NOT NULL ,
"NOTICE_ID" NUMBER(9) NULL ,
"CREATE_TIME" DATE NULL ,
"CREATE_USER_ID" NUMBER(11) NULL ,
"LAST_UPDATE_TIME" DATE NULL ,
"UPDATE_USER_ID" NUMBER(11) NULL ,
"CREATE_USER_NAME" NVARCHAR2(255) NULL ,
"UPDATE_USER_NAME" NVARCHAR2(255) NULL ,
"ABOLISHED" NUMBER(1) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "QEWEB_NOTICE_LOOK"."ABOLISHED" IS '废除标记';

-- ----------------------------
-- Indexes structure for table QEWEB_NOTICE_LOOK
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_NOTICE_LOOK
-- ----------------------------
ALTER TABLE "QEWEB_NOTICE_LOOK" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_NOTICE_LOOK
-- ----------------------------
ALTER TABLE "QEWEB_NOTICE_LOOK" ADD PRIMARY KEY ("ID");
