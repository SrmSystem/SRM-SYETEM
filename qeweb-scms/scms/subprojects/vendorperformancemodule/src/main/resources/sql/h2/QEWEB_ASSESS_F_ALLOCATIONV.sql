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

Date: 2015-08-12 13:04:07
*/


-- ----------------------------
-- Table structure for QEWEB_ASSESS_F_ALLOCATIONV
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_F_ALLOCATIONV";
CREATE TABLE "QEWEB_ASSESS_F_ALLOCATIONV" (
"ID" NUMBER(9) NULL ,
"ALLOCATION_ID" NUMBER(9) NULL ,
"VALUE_TYPE" NUMBER(9) NULL ,
"FORMULAS_SQL" NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "QEWEB_ASSESS_F_ALLOCATIONV"."ID" IS '主键';
COMMENT ON COLUMN "QEWEB_ASSESS_F_ALLOCATIONV"."ALLOCATION_ID" IS '名称';
