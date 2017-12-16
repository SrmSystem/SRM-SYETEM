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

Date: 2015-07-23 15:02:16
*/


-- ----------------------------
-- Table structure for QEWEB_SUBJECT
-- ----------------------------
DROP TABLE "QEWEB_SUBJECT";
CREATE TABLE "QEWEB_SUBJECT" (
"ID" NUMBER(11) NOT NULL ,
"TITLE" NVARCHAR2(255) NULL ,
"QUES_ID" NUMBER(11) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Indexes structure for table QEWEB_SUBJECT
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_SUBJECT
-- ----------------------------
ALTER TABLE "QEWEB_SUBJECT" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_SUBJECT
-- ----------------------------
ALTER TABLE "QEWEB_SUBJECT" ADD PRIMARY KEY ("ID");
