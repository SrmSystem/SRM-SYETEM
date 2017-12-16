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

Date: 2015-08-20 09:51:53
*/


-- ----------------------------
-- Table structure for QEWEB_VENDOR_NOTICE_CFG
-- ----------------------------
DROP TABLE "QEWEB_VENDOR_NOTICE_CFG";
CREATE TABLE "QEWEB_VENDOR_NOTICE_CFG" (
"ID" NUMBER(9) NOT NULL ,
"NOTICE_ID" NUMBER(9) NULL ,
"VENDOR_ID" NUMBER(9) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Indexes structure for table QEWEB_VENDOR_NOTICE_CFG
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_VENDOR_NOTICE_CFG
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_NOTICE_CFG" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_VENDOR_NOTICE_CFG
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_NOTICE_CFG" ADD PRIMARY KEY ("ID");
