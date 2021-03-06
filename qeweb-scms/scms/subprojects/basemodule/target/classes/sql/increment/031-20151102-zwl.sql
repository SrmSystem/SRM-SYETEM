CREATE TABLE QEWEB_DRAW_INFO (
VENDOR_ID NUMBER NULL ,
SEND_DESC NVARCHAR2(200) NULL ,
SEND_TIME TIMESTAMP(6)  NULL ,
SEND_USER_ID NUMBER NULL ,
VIEW_FLAG NUMBER NULL ,
VIEW_TIME TIMESTAMP(6)  NULL ,
CREATE_USER_ID NUMBER(11) NULL ,
CREATE_USER_NAME NVARCHAR2(255) NULL ,
CREATE_TIME DATE NULL ,
UPDATE_USER_ID NUMBER(11) NULL ,
UPDATE_USER_NAME NVARCHAR2(255) NULL ,
LAST_UPDATE_TIME DATE NULL ,
ABOLISHED NUMBER(1) NOT NULL ,
ID NUMBER NOT NULL 
);
CREATE TABLE QEWEB_DRAW_ITEM_INFO (
DRAW_ID NUMBER NULL ,
NAME NVARCHAR2(100) NULL ,
DATE_TYPE NUMBER NULL ,
PATENT_ID NUMBER NULL ,
DOWN_FLAG NUMBER NULL ,
DOWN_TIME TIMESTAMP(6)  NULL ,
CREATE_USER_ID NUMBER(11) NULL ,
CREATE_USER_NAME NVARCHAR2(255) NULL ,
CREATE_TIME DATE NULL ,
UPDATE_USER_ID NUMBER(11) NULL ,
UPDATE_USER_NAME NVARCHAR2(255) NULL ,
LAST_UPDATE_TIME DATE NULL ,
ABOLISHED NUMBER(1) NOT NULL ,
ID NUMBER NOT NULL 
);
CREATE TABLE QEWEB_DRAW_VENDOR_INFO (
VENDOR_ID NUMBER NULL ,
FACTORY_ID NUMBER NULL ,
CHECK_STATUS NUMBER NULL ,
CREATE_USER_ID NUMBER(11) NULL ,
CREATE_USER_NAME NVARCHAR2(255) NULL ,
CREATE_TIME DATE NULL ,
UPDATE_USER_ID NUMBER(11) NULL ,
UPDATE_USER_NAME NVARCHAR2(255) NULL ,
LAST_UPDATE_TIME DATE NULL ,
ABOLISHED NUMBER(1) NOT NULL ,
ID NUMBER NOT NULL 
);
ALTER TABLE QEWEB_DRAW_INFO ADD CHECK (ABOLISHED IS NOT NULL);
ALTER TABLE QEWEB_DRAW_INFO ADD CHECK (ID IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_DRAW_INFO
-- ----------------------------
ALTER TABLE QEWEB_DRAW_INFO ADD PRIMARY KEY (ID);

-- ----------------------------
-- Indexes structure for table QEWEB_DRAW_ITEM_INFO
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_DRAW_ITEM_INFO
-- ----------------------------
ALTER TABLE QEWEB_DRAW_ITEM_INFO ADD CHECK (ABOLISHED IS NOT NULL);
ALTER TABLE QEWEB_DRAW_ITEM_INFO ADD CHECK (ID IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_DRAW_ITEM_INFO
-- ----------------------------
ALTER TABLE QEWEB_DRAW_ITEM_INFO ADD PRIMARY KEY (ID);

-- ----------------------------
-- Indexes structure for table QEWEB_DRAW_VENDOR_INFO
-- ----------------------------

-- ----------------------------
-- Checks structure for table QEWEB_DRAW_VENDOR_INFO
-- ----------------------------
ALTER TABLE QEWEB_DRAW_VENDOR_INFO ADD CHECK (ABOLISHED IS NOT NULL);
ALTER TABLE QEWEB_DRAW_VENDOR_INFO ADD CHECK (ID IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table QEWEB_DRAW_VENDOR_INFO
-- ----------------------------
ALTER TABLE QEWEB_DRAW_VENDOR_INFO ADD PRIMARY KEY (ID);