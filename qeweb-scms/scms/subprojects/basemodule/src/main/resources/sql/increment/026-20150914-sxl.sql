/*
Navicat Oracle Data Transfer
Oracle Client Version : 10.2.0.5.0

Source Server         : 外网福田dome
Source Server Version : 110200
Source Host           : 221.224.132.210:5021
Source Schema         : FOTON_DEMO

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2015-09-14 13:44:46
*/


-- ----------------------------
-- Table structure for QEWEB_USER_ANSWER
-- ----------------------------

CREATE TABLE "QEWEB_USER_ANSWER" (
"ID" NUMBER(9) NOT NULL ,
"ANSWER_ID" NUMBER(9) NULL ,
"QUESTIONNAIRE_ID" NUMBER(9) NULL ,
"CONTENT" NVARCHAR2(255) NULL ,
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
COMMENT ON COLUMN "QEWEB_USER_ANSWER"."ABOLISHED" IS '废除标记';

-- ----------------------------
-- Indexes structure for table QEWEB_USER_ANSWER
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_USER_ANSWER
-- ----------------------------
ALTER TABLE "QEWEB_USER_ANSWER" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_USER_ANSWER
-- ----------------------------
ALTER TABLE "QEWEB_USER_ANSWER" ADD PRIMARY KEY ("ID");
