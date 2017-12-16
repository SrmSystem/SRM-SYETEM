CREATE TABLE QEWEB_EP_WHOLE_QUO_HIS
(
  BUYER_ID                  NUMBER(11),
  ID                         NUMBER(11)   PRIMARY KEY       NOT NULL,
  WHOLE_QUOTATION_ID         NUMBER(11),
  ENQUIRE_PRICE_ID           NUMBER(11),
  ENQUIRE_PRICE_VENDOR_ID    NUMBER(11),
  ENQUIRE_PRICE_MATERIAL_ID  NUMBER(11),
  QUOTE_COUNT                NUMBER,
  QUOTE_END_TIME             DATE,
  QUOTE_STATUS               NUMBER             DEFAULT 0,
  REQUOTE_STATUS             NUMBER,
  QUOTE_PRICE                NUMBER(17,6),
  TOTAL_QUOTE_PRICE          NUMBER(17,6),
  NEGOTIATED_PRICE           NUMBER(17,6),
  TOTAL_NEGOTIATED_PRICE     NUMBER(17,6),
  DELIVERY_DAYS              NUMBER,
  NEGOTIATED_STATUS          NUMBER             DEFAULT 0,
  NEGOTIATED_TIME            DATE,
  NEGOTIATED_USER_ID         NUMBER(11),
  NEGOTIATED_CHECK_STATUS    NUMBER,
  NEGOTIATED_CHECK_TIME      DATE,
  NEGOTIATED_CHECK_USER_ID   NUMBER(11),
  SUPPLY_CYCLE               VARCHAR2(50 BYTE),
  BRAND                      VARCHAR2(100 BYTE),
  MATERIAL_COMPOSITION       VARCHAR2(200 BYTE),
  WARRANTY_PERIOD            VARCHAR2(100 BYTE),
  TRANSPORTATION_MODE        VARCHAR2(100 BYTE),
  PAYMENT_MEANS              VARCHAR2(100 BYTE),
  TAX_RATE                   NUMBER(17,6),
  TECHNOLOGY_PROMISES        VARCHAR2(300 BYTE),
  QUALITY_PROMISES           VARCHAR2(300 BYTE),
  SERVICE_PROMISES           VARCHAR2(300 BYTE),
  DELIVERY_PROMISES          VARCHAR2(300 BYTE),
  OTHER_PROMISES             VARCHAR2(300 BYTE),
  MPQ                        NUMBER(17,6),
  MOQ                        NUMBER(17,6),
  REMARKS                    VARCHAR2(500 BYTE),
  PUBLISH_STATUS             NUMBER,
  PUBLISH_USER_ID            NUMBER(11),
  PUBLISH_TIME               DATE,
  EIP_APPROVAL_STATUS        NUMBER,
  EIP_APPROVAL_USER_ID       NUMBER(11),
  EIP_APPROVAL_TIME          DATE,
  CLOSE_STATUS               NUMBER(11),
  CLOSE_TIME                 DATE,
  CLOSE_USER_ID              NUMBER(11),
  ABOLISHED                  NUMBER(1),
  CREATE_TIME                TIMESTAMP(6),
  CREATE_USER_ID             NUMBER(9),
  LAST_UPDATE_TIME           TIMESTAMP(6),
  UPDATE_USER_ID             NUMBER(9),
  CREATE_USER_NAME           NVARCHAR2(100),
  UPDATE_USER_NAME           NVARCHAR2(100),
  APPLICATION_STATUS         NUMBER(1),
  MANUFACTURER               VARCHAR2(100 BYTE),
  BEAR_FREIGHT_CHARGES_BY    VARCHAR2(100 BYTE),
  QUOTE_TEMPLATE_URL         VARCHAR2(100 BYTE),
  QUOTE_TEMPLATE_NAME        VARCHAR2(100 BYTE),
  TAX_CATEGORY               VARCHAR2(100 BYTE),
  COOPERATION_QTY            NUMBER(10),
  COOPERATION_STATUS         NUMBER(1)
);
CREATE TABLE QEWEB_EP_WHOLE_QUO
(
  ID                         NUMBER(11)   PRIMARY KEY       NOT NULL,
  ENQUIRE_PRICE_ID           NUMBER(11),
  ENQUIRE_PRICE_VENDOR_ID    NUMBER(11),
  ENQUIRE_PRICE_MATERIAL_ID  NUMBER(11),
  QUOTE_COUNT                NUMBER,
  QUOTE_END_TIME             DATE,
  QUOTE_STATUS               NUMBER             DEFAULT 0,
  REQUOTE_STATUS             NUMBER,
  QUOTE_PRICE                NUMBER(17,6),
  TOTAL_QUOTE_PRICE          NUMBER(17,6),
  NEGOTIATED_PRICE           NUMBER(17,6),
  TOTAL_NEGOTIATED_PRICE     NUMBER(17,6),
  DELIVERY_DAYS              NUMBER,
  NEGOTIATED_STATUS          NUMBER             DEFAULT 0,
  NEGOTIATED_TIME            DATE,
  NEGOTIATED_USER_ID         NUMBER(11),
  NEGOTIATED_CHECK_STATUS    NUMBER,
  NEGOTIATED_CHECK_TIME      DATE,
  NEGOTIATED_CHECK_USER_ID   NUMBER(11),
  SUPPLY_CYCLE               VARCHAR2(50 BYTE),
  BRAND                      VARCHAR2(100 BYTE),
  MATERIAL_COMPOSITION       VARCHAR2(200 BYTE),
  WARRANTY_PERIOD            VARCHAR2(100 BYTE),
  TRANSPORTATION_MODE        VARCHAR2(100 BYTE),
  PAYMENT_MEANS              VARCHAR2(100 BYTE),
  TAX_RATE                   NUMBER(17,6),
  TECHNOLOGY_PROMISES        VARCHAR2(300 BYTE),
  QUALITY_PROMISES           VARCHAR2(300 BYTE),
  SERVICE_PROMISES           VARCHAR2(300 BYTE),
  DELIVERY_PROMISES          VARCHAR2(300 BYTE),
  OTHER_PROMISES             VARCHAR2(300 BYTE),
  MPQ                        NUMBER(17,6),
  MOQ                        NUMBER(17,6),
  REMARKS                    VARCHAR2(500 BYTE),
  PUBLISH_STATUS             NUMBER,
  PUBLISH_USER_ID            NUMBER(11),
  PUBLISH_TIME               DATE,
  EIP_APPROVAL_STATUS        NUMBER,
  EIP_APPROVAL_USER_ID       NUMBER(11),
  EIP_APPROVAL_TIME          DATE,
  CLOSE_STATUS               NUMBER,
  CLOSE_TIME                 DATE,
  CLOSE_USER_ID              NUMBER(11),
  ABOLISHED                  NUMBER(1),
  CREATE_TIME                TIMESTAMP(6),
  CREATE_USER_ID             NUMBER(9),
  LAST_UPDATE_TIME           TIMESTAMP(6),
  UPDATE_USER_ID             NUMBER(9),
  CREATE_USER_NAME           NVARCHAR2(100),
  BUYER_ID                  NUMBER(11),
  UPDATE_USER_NAME           NVARCHAR2(100),
  APPLICATION_STATUS         NUMBER(1),
  MANUFACTURER               VARCHAR2(100 BYTE),
  BEAR_FREIGHT_CHARGES_BY    VARCHAR2(100 BYTE),
  QUOTE_TEMPLATE_URL         VARCHAR2(100 BYTE),
  QUOTE_TEMPLATE_NAME        VARCHAR2(100 BYTE),
  TAX_CATEGORY               VARCHAR2(100 BYTE),
  COOPERATION_QTY            NUMBER(10),
  COOPERATION_STATUS         NUMBER(1),
  IS_VENDOR                  NUMBER(1),
  EIP_STATUS                 NUMBER,
  IS_INCLUDE_TAX             VARCHAR2(100 BYTE),
  EIP_APPROVE_REMARK         NVARCHAR2(256),
  PRICE_CHANGE_NUM           NUMBER
);
CREATE TABLE QEWEB_EP_VENDOR
(
  ID                     NUMBER(11)   PRIMARY KEY       NOT NULL,
  ENQUIRE_PRICE_ID       NUMBER(11),
  VENDOR_ID              NUMBER(11),
  VENDOR_CODE            VARCHAR2(255 BYTE),
  VENDOR_NAME            VARCHAR2(255 BYTE),
  ADDRESS                VARCHAR2(300 BYTE),
  LEGAL_REP              VARCHAR2(100 BYTE),
  LINK_PHONE             VARCHAR2(100 BYTE),
  BUSINESS_LINK_PHONE    VARCHAR2(100 BYTE),
  ACCESS_STATUS          NUMBER,
  ORG_EMAIL              VARCHAR2(100 BYTE),
  SAP_CODE               VARCHAR2(100 BYTE),
  ABOLISHED              NUMBER(1),
  CREATE_TIME            TIMESTAMP(6),
  CREATE_USER_ID         NUMBER(9),
  LAST_UPDATE_TIME       TIMESTAMP(6),
  UPDATE_USER_ID         NUMBER(9),
  CREATE_USER_NAME       NVARCHAR2(100),
  UPDATE_USER_NAME       NVARCHAR2(100),
  BUYER_ID              NUMBER(11),
  COOPERAT_STATUS        NUMBER(1),
  APPLICATION_STATUS     NUMBER(1),
  EIP_APPROVAL_STATUS    NUMBER(11),
  EIP_APPROVAL_TIME      DATE,
  EIP_APPROVAL_USER_ID   NUMBER(11),
  EIP_APPROVE_REMARK     NVARCHAR2(100),
  INSTANCE_ID            NVARCHAR2(100),
  QUOTE_STATUS           NUMBER,
  SPECIAL_PURCHASE_TYPE  NVARCHAR2(26)
);
CREATE TABLE QEWEB_EP_SUB_QUO_HIS
(
  ID                    NUMBER(11)   PRIMARY KEY       NOT NULL,
  SUBITEM_QUOTATION_ID  NUMBER(11),
  MATERIAL_NAME         VARCHAR2(200 BYTE),
  MATERIAL_SPEC         VARCHAR2(200 BYTE),
  QTY                   NUMBER(17,6),
  MATERIAL_UNIT         VARCHAR2(200 BYTE),
  TOTAL_QUOTE_PRICE     NUMBER(17,6),
  QUOTE_PRICE           NUMBER(17,6),
  BRAND                 VARCHAR2(100 BYTE),
  REMARKS               VARCHAR2(500 BYTE),
  ABOLISHED             NUMBER(1),
  CREATE_TIME           TIMESTAMP(6),
  CREATE_USER_ID        NUMBER(9),
  LAST_UPDATE_TIME      TIMESTAMP(6),
  UPDATE_USER_ID        NUMBER(9),
  CREATE_USER_NAME      NVARCHAR2(100),
  UPDATE_USER_NAME      NVARCHAR2(100),
  BUYER_ID             NUMBER(11),
  MANAGEMENT_FEE_RATE   NUMBER(17,6),
  PROFIT_RATE           NUMBER(17,6),
  PROFIT                NUMBER(17,6),
  TAX_RATE              NUMBER(17,6),
  TAX_FEE               NUMBER(17,6),
  CARRIAGE_CHARGES      NUMBER(17,6),
  SUBTOTAL              NUMBER(17,6),
  WHOLE_QUO_ID          NUMBER
);
CREATE TABLE QEWEB_EP_SUB_QUO
(
  ID                          NUMBER(11)   PRIMARY KEY       NOT NULL,
  WHOLE_QUOTATION_ID          NUMBER(11),
  MATERIAL_NAME               VARCHAR2(200 BYTE),
  MATERIAL_SPEC               VARCHAR2(200 BYTE),
  QTY                         NUMBER(17,6),
  MATERIAL_UNIT               VARCHAR2(200 BYTE),
  TOTAL_QUOTE_PRICE           NUMBER(17,6),
  QUOTE_PRICE                 NUMBER(17,6),
  BRAND                       VARCHAR2(100 BYTE),
  REMARKS                     VARCHAR2(500 BYTE),
  ABOLISHED                   NUMBER(1),
  CREATE_TIME                 TIMESTAMP(6),
  CREATE_USER_ID              NUMBER(9),
  LAST_UPDATE_TIME            TIMESTAMP(6),
  UPDATE_USER_ID              NUMBER(9),
  CREATE_USER_NAME            NVARCHAR2(100),
  UPDATE_USER_NAME            NVARCHAR2(100),
  MODULE_ITEM_ID              NUMBER(11),
  TENANT_ID                   NUMBER(11),
  MANAGEMENT_FEE_RATE         NUMBER(17,6),
  PROFIT_RATE                 NUMBER(17,6),
  PROFIT                      NUMBER(17,6),
  TAX_RATE                    NUMBER(17,6),
  TAX_FEE                     NUMBER(17,6),
  CARRIAGE_CHARGES            NUMBER(17,6),
  SUBTOTAL                    NUMBER(17,6),
  IS_VENDOR                   NUMBER,
  NEGOTIATED_SUB_PRICE        NUMBER,
  BUYER_ID             NUMBER(11),
  NEGOTIATED_SUB_TOTAL_PRICE  NUMBER
);
CREATE TABLE QEWEB_EP_QUO_SUB_ITEM
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  PARENT_ID         NUMBER(9),
  EP_MATERIAL_ID    NUMBER(9),
  NAME              VARCHAR2(200 BYTE),
  UNIT_ID           VARCHAR2(56 BYTE),
  REMARKS           VARCHAR2(500 BYTE),
  IS_TOP            NUMBER(1),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100),
  BUYER_ID             NUMBER(11)
);
CREATE TABLE QEWEB_EP_QUO_SUB_COST
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  EP_QUO_SUB_ID     NUMBER(9),
  NAME              VARCHAR2(200 BYTE),
  UNIT              VARCHAR2(200 BYTE),
  QTY               NUMBER,
  PRICE             NUMBER,
  TOTAL_PRICE       NUMBER,
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100),
  BUYER_ID             NUMBER(11),
  IS_VENDOR         NUMBER,
  EP_QUO_WHOLE_ID   NUMBER
);
CREATE TABLE QEWEB_EP_PRICE
(
  ID                        NUMBER(11)   PRIMARY KEY       NOT NULL,
  ORDER_ID                  NUMBER(11),
  ENQUIRE_PRICE_CODE        VARCHAR2(50 BYTE),
  PROJECT_NAME              VARCHAR2(100 BYTE),
  QUOTE_END_TIME            DATE,
  MATERIAL_TYPE             NUMBER,
  QUOTE_WAY                 NUMBER,
  JOIN_WAY                  NUMBER,
  RESULT_OPEN_WAY           NUMBER              DEFAULT 0,
  AUDIT_STATUS              NUMBER              DEFAULT 0,
  AUDIT_TIME                DATE,
  AUDIT_USER_ID             NUMBER(11),
  PUBLISH_STATUS            NUMBER              DEFAULT 0,
  PUBLISH_TIME              DATE,
  PUBLISH_USER_ID           NUMBER(11),
  QUOTE_STATUS              NUMBER              DEFAULT 0,
  NEGOTIATION_STATUS        NUMBER              DEFAULT 0,
  CLOSE_STATUS              NUMBER              DEFAULT 0,
  CLOSE_TIME                DATE,
  CLOSE_USER_ID             NUMBER(11),
  REMARKS                   VARCHAR2(500 BYTE),
  ABOLISHED                 NUMBER(1),
  CREATE_TIME               TIMESTAMP(6),
  CREATE_USER_ID            NUMBER(9),
  LAST_UPDATE_TIME          TIMESTAMP(6),
  UPDATE_USER_ID            NUMBER(9),
  CREATE_USER_NAME          NVARCHAR2(100),
  UPDATE_USER_NAME          NVARCHAR2(100),
  BUYER_ID             NUMBER(11),
  APPLICATION_DEADLINE      DATE,
  AUDIT_COMMENTS            VARCHAR2(255 BYTE),
  APPLICATION_STATUS        NUMBER(10),
  QUOTE_SPECIFICATION_URL   VARCHAR2(255 BYTE),
  QUOTE_TEMPLATE_URL        VARCHAR2(255 BYTE),
  QUOTE_SPEC_FILE_NAME      VARCHAR2(255 BYTE),
  QUOTE_TEMPLATE_FILE_NAME  VARCHAR2(255 BYTE),
  IS_TOP                    NUMBER(1),
  IS_VENDOR                 NUMBER(1),
  QUOTE_TYPE                NUMBER(10),
  SIGN_PERSON1_ID           NUMBER(10),
  SIGN_PERSON2_ID           NUMBER(10),
  SIGN_PERSON3_ID           NUMBER(10),
  SIGN_PERSON4_ID           NUMBER(10),
  SIGN_PERSON5_ID           NUMBER(10),
  PRICE_START_TIME          DATE,
  PRICE_END_TIME            DATE,
  FACTORY                   NVARCHAR2(200),
  CHECK_DEP                 NUMBER,
  IS_DIM                    NUMBER(1)
);
CREATE TABLE QEWEB_EP_MODULE_MATERIAL_REL
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  MATERIAL_ID       NUMBER(11),
  MODULE_ID         NUMBER(11),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100),
  BUYER_ID             NUMBER(11),
  SQENUM            NUMBER(3)
);
CREATE TABLE QEWEB_EP_MODULE_ITEM
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  PARENT_ID         NUMBER(9),
  MODULE_ID         NUMBER(9),
  NAME              VARCHAR2(200 BYTE),
  UNIT_ID           VARCHAR2(56 BYTE),
  REMARKS           VARCHAR2(500 BYTE),
  IS_TOP            NUMBER(1),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100),
  BUYER_ID             NUMBER(11)
);
CREATE TABLE QEWEB_EP_MODULE
(
  ID                NUMBER(11)   PRIMARY KEY       NOT NULL,
  CODE              VARCHAR2(200 BYTE),
  NAME              VARCHAR2(200 BYTE),
  REMARKS           VARCHAR2(500 BYTE),
  IS_DEFAULT        NUMBER(1),
  ABOLISHED         NUMBER(1),
  CREATE_TIME       TIMESTAMP(6),
  CREATE_USER_ID    NUMBER(9),
  LAST_UPDATE_TIME  TIMESTAMP(6),
  UPDATE_USER_ID    NUMBER(9),
  CREATE_USER_NAME  NVARCHAR2(100),
  UPDATE_USER_NAME  NVARCHAR2(100),
  BUYER_ID             NUMBER(11)
);
CREATE TABLE QEWEB_EP_MATERIAL
(
  ID                            NUMBER(11)   PRIMARY KEY       NOT NULL,
  ENQUIRE_PRICE_ID              NUMBER(11),
  CATEGORY_ID                   NUMBER(11),
  CATEGORY_CODE                 VARCHAR2(200 BYTE),
  CATEGORY_NAME                 VARCHAR2(200 BYTE),
  MATERIAL_ID                   NUMBER(11),
  MATERIAL_CODE                 VARCHAR2(200 BYTE),
  MATERIAL_NAME                 VARCHAR2(200 BYTE),
  MATERIAL_SPEC                 VARCHAR2(200 BYTE),
  MATERIAL_UNIT                 VARCHAR2(200 BYTE),
  EXPECTED_BRAND                VARCHAR2(200 BYTE),
  MATERIAL_COMPOSITION          VARCHAR2(200 BYTE),
  PLAN_PURCHASE_QTY             NUMBER(17,6),
  REMARKS                       VARCHAR2(500 BYTE),
  ABOLISHED                     NUMBER(1),
  CREATE_TIME                   TIMESTAMP(6),
  CREATE_USER_ID                NUMBER(9),
  LAST_UPDATE_TIME              TIMESTAMP(6),
  UPDATE_USER_ID                NUMBER(9),
  CREATE_USER_NAME              NVARCHAR2(100),
  UPDATE_USER_NAME              NVARCHAR2(100),
  MODULE_ID                     NUMBER(11),
  BUYER_ID             NUMBER(11),
  WARRANTY                      VARCHAR2(255 CHAR),
  MANUFACTURER                  VARCHAR2(255 BYTE),
  ARRIVAL_TIME                  DATE,
  LOWEST_COOP_PRICE             NUMBER(10),
  CURRENT_LOWEST_QUOTE_PRICE    NUMBER(10),
  CURRENT_HIGHTEST_QUOTE_PRICE  NUMBER(10),
  FREIGHT                       NUMBER(17,6),
  COMPARE_MATERIAL_CODE         NVARCHAR2(255),
  COMPARE_MATERIAL_PRICE        NUMBER(10),
  COMPARE_VENDOR_NAME           NVARCHAR2(255),
  PRODUCT_STATUS_DIFFER         NVARCHAR2(255)
);