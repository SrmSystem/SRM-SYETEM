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

Date: 2015-08-12 13:03:49
*/


-- ----------------------------
-- Table structure for QEWEB_ASSESS_F_ALLOCATION
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_F_ALLOCATION";
CREATE TABLE "QEWEB_ASSESS_F_ALLOCATION" (
"ID" NUMBER(9) NULL ,
"NAME" NVARCHAR2(50) NULL ,
"DESCRIBE" NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "QEWEB_ASSESS_F_ALLOCATION"."ID" IS '主键';
COMMENT ON COLUMN "QEWEB_ASSESS_F_ALLOCATION"."NAME" IS '名称';
