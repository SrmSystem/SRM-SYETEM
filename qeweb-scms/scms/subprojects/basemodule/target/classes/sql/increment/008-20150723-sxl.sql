
DROP TABLE "QEWEB_ANSWER";
CREATE TABLE "QEWEB_ANSWER" (
"ID" NUMBER(11) NOT NULL ,
"TITLE" NVARCHAR2(255) NULL ,
"TYPE" NUMBER(1) NULL ,
"SUBJECT_ID" NUMBER(11) NULL ,
"CHOICE_NUMBER" NUMBER(11) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Indexes structure for table QEWEB_ANSWER
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_ANSWER
-- ----------------------------
ALTER TABLE "QEWEB_ANSWER" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_ANSWER
-- ----------------------------
ALTER TABLE "QEWEB_ANSWER" ADD PRIMARY KEY ("ID");
