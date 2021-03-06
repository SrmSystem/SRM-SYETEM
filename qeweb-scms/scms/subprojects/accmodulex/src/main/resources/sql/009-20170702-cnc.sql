CREATE TABLE QEWEB_FILE_SYNERGY
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  NAME              NVARCHAR2(100),
  SYNERGY_TYPE         NUMBER(1),
  REMARKS           NVARCHAR2(100),  
  START_TIME        TIMESTAMP(6),
  END_TIME          TIMESTAMP(6),
  PUBLISH_STATUS    NUMBER(1),
  PUBLISH_USER_ID   NUMBER(11),
  SYNERGY_FILE      BLOB(4000),
  REMARKS           VARCHAR(255),
  
 
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
);

CREATE TABLE QEWEB_SEND_BACK
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  VENDOR_ID         NUMBER(11),
  SEND_BACK_TYPE    NUMBER(1),
  SEND_BACK_TIME    TIMESTAMP(6),
  SEND_BACK_FILE    BLOB(4000),
  FILE_SYNERGY_ID   NUMBER(11),
  
  
   ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100)
  

);