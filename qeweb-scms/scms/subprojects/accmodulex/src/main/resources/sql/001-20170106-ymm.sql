CREATE TABLE QEWEB_BUYER_MATERIAL_REL
(
  ID                NUMBER(9)   PRIMARY KEY       NOT NULL,
  BUYER_ID       	NUMBER(9),
  MATERIAL_ID          NUMBER(9),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);
CREATE TABLE QEWEB_BUYER_MATERIAL_TYPE_REL
(
  ID                NUMBER(9)   PRIMARY KEY       NOT NULL,
  BUYER_ID       	NUMBER(9),
  MATERIAL_TYPE_ID          NUMBER(9),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);


