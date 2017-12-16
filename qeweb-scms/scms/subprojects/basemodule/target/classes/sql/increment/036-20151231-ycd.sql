/**
 * 角色数据权限
 */
CREATE TABLE QEWEB_ROLE_DATA
(
  ID                NUMBER(19)     PRIMARY KEY             NOT NULL,
  DATA_IDS          VARCHAR2(2000 BYTE),
  ROLE_DATA_CFG_ID  NUMBER(19),
  ROLE_ID           NUMBER(19),
  REMARK            VARCHAR2(255 BYTE)
);

/**
 * 数据权限配置
 */
CREATE TABLE QEWEB_ROLE_DATA_CFG
(
  ID          NUMBER(19)            PRIMARY KEY            NOT NULL,
  DATA_CODE   NUMBER(10)                        NOT NULL,
  DATA_NAME   VARCHAR2(255 BYTE)                NOT NULL,
  DATA_CLAZZ  VARCHAR2(255 BYTE)                NOT NULL,
  DATA_SCOPE  VARCHAR2(255 BYTE)                NOT NULL,
  REMARK      VARCHAR2(255 BYTE)
);

/**
 * 过滤配置
 */
CREATE TABLE QEWEB_QUERY_FILTER_CFG
(
  ID          NUMBER(19)            PRIMARY KEY            NOT NULL,
  CLAZZ       VARCHAR2(255 BYTE)                NOT NULL,
  DATA_NAMES  VARCHAR2(255 BYTE)                NOT NULL,
  DATA_TYPES  VARCHAR2(255 BYTE)                NOT NULL,
  REMARK      VARCHAR2(255 BYTE)
);