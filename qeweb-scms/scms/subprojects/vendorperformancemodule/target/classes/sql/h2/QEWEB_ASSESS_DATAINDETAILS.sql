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

Date: 2015-09-02 10:03:23
*/


-- ----------------------------
-- Table structure for QEWEB_ASSESS_DATAINDETAILS
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_DATAINDETAILS";
CREATE TABLE "QEWEB_ASSESS_DATAINDETAILS" (
"ID" NUMBER(9) NOT NULL ,
"CYCLE_ID" NUMBER(9) NULL ,
"ASSESS_DATE" TIMESTAMP(6)  NULL ,
"BELONG_DATE" TIMESTAMP(6)  NULL ,
"VENDOR_CODE" NVARCHAR2(255) NULL ,
"VENDOR_NAME" NVARCHAR2(255) NULL ,
"BRAND_NAME" NVARCHAR2(255) NULL ,
"FACTORY_NAME" NVARCHAR2(255) NULL ,
"MATERIAL_CODE" NVARCHAR2(100) NULL ,
"MATERIAL_NAME" NVARCHAR2(100) NULL ,
"INDEX_NAME" NVARCHAR2(255) NULL ,
"DIMENSIONS_NAME" NVARCHAR2(255) NULL ,
"MUST_IMPORT_ELEMENTS" NVARCHAR2(255) NULL ,
"NO_IMPORT_ELEMENTS" NVARCHAR2(255) NULL ,
"NO_IMPORT_ELEMENTS_NAME" NVARCHAR2(255) NULL ,
"CYCLE_NAME" NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Indexes structure for table QEWEB_ASSESS_DATAINDETAILS
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_ASSESS_DATAINDETAILS
-- ----------------------------
ALTER TABLE "QEWEB_ASSESS_DATAINDETAILS" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_ASSESS_DATAINDETAILS
-- ----------------------------
ALTER TABLE "QEWEB_ASSESS_DATAINDETAILS" ADD PRIMARY KEY ("ID");
