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

Date: 2015-07-17 09:38:08
*/


-- ----------------------------
-- Table structure for QEWEB_VENDOR_REPORT
-- ----------------------------
DROP TABLE "QEWEB_VENDOR_REPORT";
CREATE TABLE "QEWEB_VENDOR_REPORT" (
"ID" NUMBER(11) NOT NULL ,
"REPORT_NAME" NVARCHAR2(255) NULL ,
"REPORT_URL" VARCHAR2(255 BYTE) NULL ,
"CREATE_TIME" DATE NULL ,
"CREATE_USER_ID" NUMBER(11) NULL ,
"LAST_UPDATE_TIME" DATE NULL ,
"UPDATE_USER_ID" NUMBER(11) NULL ,
"CREATE_USER_NAME" NVARCHAR2(255) NULL ,
"UPDATE_USER_NAME" NVARCHAR2(255) NULL ,
"ABOLISHED" NUMBER(1) NULL ,
"ORGID" NUMBER(11) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "QEWEB_VENDOR_REPORT"."ABOLISHED" IS '废除标记';

-- ----------------------------
-- Indexes structure for table QEWEB_VENDOR_REPORT
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_VENDOR_REPORT
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_REPORT" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "QEWEB_VENDOR_REPORT" ADD CHECK ("ORGID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_VENDOR_REPORT
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_REPORT" ADD PRIMARY KEY ("ID");
