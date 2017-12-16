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

Date: 2015-08-12 13:04:25
*/


-- ----------------------------
-- Table structure for QEWEB_ASSESS_FORMULAS
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_FORMULAS";
CREATE TABLE "QEWEB_ASSESS_FORMULAS" (
"ID" NUMBER(9) NULL ,
"CONTENT" NVARCHAR2(50) NULL ,
"INDEX_ID" NUMBER(9) NULL ,
"CYCLE_ID" NUMBER(9) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "QEWEB_ASSESS_FORMULAS" IS 'qeweb_assess_formulas';
COMMENT ON COLUMN "QEWEB_ASSESS_FORMULAS"."ID" IS '主键';
COMMENT ON COLUMN "QEWEB_ASSESS_FORMULAS"."CONTENT" IS '名称';
