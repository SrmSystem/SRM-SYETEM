

-- ----------------------------
-- Table structure for QEWEB_ASSESS_MAPPED
-- ----------------------------
DROP TABLE "QEWEB_ASSESS_MAPPED";
CREATE TABLE "QEWEB_ASSESS_MAPPED" (
"ID" NUMBER(9) NOT NULL ,
"NAME" NVARCHAR2(255) NULL ,
"DESCRIBE" NVARCHAR2(255) NULL ,
"MAPPED_TYPE" NUMBER(1) NULL ,
"MAPPED_VALUE" NVARCHAR2(255) NULL ,
"MAPPED_NAME" NVARCHAR2(255) NULL 
);

-- ----------------------------
-- Indexes structure for table QEWEB_ASSESS_MAPPED
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_ASSESS_MAPPED
-- ----------------------------
ALTER TABLE "QEWEB_ASSESS_MAPPED" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_ASSESS_MAPPED
-- ----------------------------
ALTER TABLE "QEWEB_ASSESS_MAPPED" ADD PRIMARY KEY ("ID");
