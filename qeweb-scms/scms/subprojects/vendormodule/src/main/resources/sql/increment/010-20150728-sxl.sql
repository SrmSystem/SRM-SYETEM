/*
Navicat Oracle Data Transfer
Oracle Client Version : 10.2.0.5.0

Source Server         : 福田测试库
Source Server Version : 110200
Source Host           : 192.168.0.250:1521
Source Schema         : FOTON

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2015-07-28 10:01:49
*/


-- ----------------------------
-- Table structure for QEWEB_VENDOR_BU_CFG
-- ----------------------------
DROP TABLE "QEWEB_VENDOR_BU_CFG";
CREATE TABLE "QEWEB_VENDOR_BU_CFG" (
"ID" NUMBER NOT NULL ,
"CODES" NVARCHAR2(255) NULL ,
"NAME" NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of QEWEB_VENDOR_BU_CFG
-- ----------------------------
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('1', '2230', '北方工程车事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('2', '2240', '南方工程车事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('3', '2250', '欧曼工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('4', '2271', '欧V新能源客车事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('5', '1000', '集团总部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('6', '2290', '奥铃工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('7', '2400', '雷萨起重机事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('8', '2200', '蒙派克工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('9', '2210', '萨普工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('10', '2100', '奥铃发动机');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('11', '2120', '雷沃动力事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('12', '2310', '雷萨泵送事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('13', '2420', '南海欧辉工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('14', '2430', '北京多功能工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('15', '2260', '模具工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('16', '1002', '培训中心');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('17', '1101', '配件公司');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('18', '2110', '铸造工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('19', '1200', '工程研究院');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('20', '2460', '山东多功能工厂');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('21', '2490', '河北福田');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('22', '5100', '福田智科');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('23', '2300', '海外事业部');
INSERT INTO "QEWEB_VENDOR_BU_CFG" VALUES ('24', '1202', 'G03工厂');

-- ----------------------------
-- Indexes structure for table QEWEB_VENDOR_BU_CFG
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_VENDOR_BU_CFG
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_BU_CFG" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_VENDOR_BU_CFG
-- ----------------------------
ALTER TABLE "QEWEB_VENDOR_BU_CFG" ADD PRIMARY KEY ("ID");
