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

Date: 2015-08-12 13:05:48
*/


-- ----------------------------
-- Table structure for QEWEB_ASSESS_TEMPLATE_WEIGHT
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_TEMPLATE_WEIGHT";
CREATE TABLE "QEWEB_ASSESS_TEMPLATE_WEIGHT" (
"ID" NUMBER(9) NULL ,
"WEIGHT_TYPE" NUMBER(1) NULL ,
"TEMPLATE_ID" NUMBER(9) NULL ,
"DIM_ID" NUMBER(9) NULL ,
"INDEX_ID" NUMBER(9) NULL ,
"WEIGHT_NUMBER" NUMBER(11) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "QEWEB_ASSESS_TEMPLATE_WEIGHT" IS 'qeweb_assess_template_weight';
COMMENT ON COLUMN "QEWEB_ASSESS_TEMPLATE_WEIGHT"."ID" IS '主键';
COMMENT ON COLUMN "QEWEB_ASSESS_TEMPLATE_WEIGHT"."WEIGHT_TYPE" IS '名称';
