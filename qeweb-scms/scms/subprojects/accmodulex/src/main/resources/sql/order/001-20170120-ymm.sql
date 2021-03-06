CREATE TABLE QEWEB_PROCESS
(
  ID                NUMBER(9)   PRIMARY KEY       NOT NULL,
  CODE       	NVARCHAR2(100),
  NAME          NVARCHAR2(100),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);
CREATE TABLE QEWEB_PROCESS_MATERIAL_REL
(
  ID                NUMBER(9)   PRIMARY KEY       NOT NULL,
  PROCESS_ID       	NUMBER(9),
  MATERIAL_ID          NUMBER(9),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);
CREATE TABLE QEWEB_ORDER_PROCESS
(
  ID                NUMBER(9)   PRIMARY KEY       NOT NULL,
  PROCESS_ID       	NUMBER(9),
  ORDER_ITEM_ID          NUMBER(9),
  PROCESS_NAME      NVARCHAR2(100),
  ORDER_NUM         NUMBER(11,2),
  ORDER_TIME        TIMESTAMP(6),
  FILE_NAME         NVARCHAR2(100),
  FILE_PATH         NVARCHAR2(100),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);



