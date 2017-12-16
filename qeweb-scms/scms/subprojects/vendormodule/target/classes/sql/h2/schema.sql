drop table if exists QEWEB_VENDOR_ANSWER;
drop table if exists QEWEB_VENDOR_BASE_INFO;
drop table if exists QEWEB_VENDOR_BASE_INFO_EX;
drop table if exists QEWEB_VENDOR_CHANGEHIS;
drop table if exists QEWEB_VENDOR_DYNC_INFO;
drop table if exists QEWEB_VENDOR_INVITE_EMAIL;
drop table if exists QEWEB_VENDOR_MATERIAL_REL;
drop table if exists QEWEB_VENDOR_MAT_SUPPLY_REL;
drop table if exists QEWEB_VENDOR_NAV_CFG;
drop table if exists QEWEB_VENDOR_NAV_TEMPLATE;
drop table if exists QEWEB_VENDOR_PHASE;
drop table if exists QEWEB_VENDOR_PHASE_CFG;
drop table if exists QEWEB_VENDOR_PROPERTY_DICT;
drop table if exists QEWEB_VENDOR_REPORT;
drop table if exists QEWEB_VENDOR_SUBJECT;
drop table if exists QEWEB_VENDOR_SUPPLY_REL;
drop table if exists QEWEB_VENDOR_SURVEY_BASE;
drop table if exists QEWEB_VENDOR_SURVEY_CFG;
drop table if exists QEWEB_VENDOR_SURVEY_DATA;
drop table if exists QEWEB_VENDOR_SURVEY_DATA_HIS;
drop table if exists QEWEB_VENDOR_SURVEY_TEMPLATE;
drop table if exists QEWEB_VENDOR_TEMPLATE_PHASE;
drop table if exists QEWEB_VENDOR_TEMPLATE_RANGE;
drop table if exists QEWEB_VENDOR_TEMPLATE_SURVEY;


create table QEWEB_VENDOR_ANSWER
(
  id            NUMBER(11) not null,
  title         NVARCHAR2(255),
  type          NUMBER(1),
  subject_id    NUMBER(11),
  choice_number NUMBER(11)
);

create table QEWEB_VENDOR_BASE_INFO
(
  id               NUMBER(9) not null,
  code             NVARCHAR2(50),
  name             NVARCHAR2(50),
  short_name       NVARCHAR2(50),
  property         NVARCHAR2(64),
  country          NUMBER(9),
  province         NUMBER(9),
  city             NUMBER(9),
  address          NVARCHAR2(50),
  material_id      NUMBER(9),
  material_type_id NUMBER(9),
  product_line     NVARCHAR2(50),
  last_year_income NVARCHAR2(50),
  employee_amount  NVARCHAR2(50),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  duns             NVARCHAR2(9),
  legal_person     NVARCHAR2(50),
  regtime          TIMESTAMP(6),
  stock_share      NVARCHAR2(50),
  main_product     NVARCHAR2(500),
  org_id           NUMBER(9),
  phase_id         NUMBER(9),
  template_id      NUMBER(9),
  ipo              NUMBER(1) default 0,
  web_addr         NVARCHAR2(50),
  tax_id           NVARCHAR2(50),
  ownership        NVARCHAR2(50),
  bank_name        NVARCHAR2(50),
  mainbu           NUMBER(9) default 0,
  reg_capital      NVARCHAR2(50),
  total_capital    NVARCHAR2(50),
  working_capital  NVARCHAR2(50),
  floor_area       NVARCHAR2(50),
  col1             NVARCHAR2(50),
  col2             NVARCHAR2(50),
  col3             NVARCHAR2(50),
  col4             NVARCHAR2(50),
  col5             NVARCHAR2(50),
  col6             NVARCHAR2(50),
  col7             NVARCHAR2(50),
  col8             NVARCHAR2(50),
  col9             NVARCHAR2(50),
  col10            NVARCHAR2(50),
  col11            NVARCHAR2(50),
  col12            NVARCHAR2(50),
  col13            NVARCHAR2(50),
  col14            NVARCHAR2(50),
  col15            NVARCHAR2(50),
  col16            NVARCHAR2(50),
  col17            NVARCHAR2(50),
  col18            NVARCHAR2(50),
  col19            NVARCHAR2(50),
  col20            NVARCHAR2(50),
  col21            NVARCHAR2(50),
  col22            NVARCHAR2(50),
  col23            NVARCHAR2(50),
  col24            NVARCHAR2(50),
  col25            NVARCHAR2(50),
  col26            NVARCHAR2(50),
  col27            NVARCHAR2(50),
  col28            NVARCHAR2(50),
  col29            NVARCHAR2(50),
  col30            NVARCHAR2(50),
  vendor_type      NUMBER(9) default 0,
  phase_cfg_id     NUMBER(9),
  phase_sn         NUMBER(2),
  district         NUMBER(9),
  country_text     VARCHAR2(50 CHAR),
  province_text    VARCHAR2(50 CHAR),
  city_text        VARCHAR2(50 CHAR),
  district_text    VARCHAR2(50 CHAR),
  product_power    NVARCHAR2(50),
  versionno        NUMBER(3),
  current_version  NUMBER(1) default 0,
  audit_status     NUMBER(1),
  approve_status   NUMBER(1),
  submit_status    NUMBER(1),
  audit_reason     NVARCHAR2(255),
  audit_user       NVARCHAR2(255),
  vendor_level     NVARCHAR2(20),
  vendor_classify  NVARCHAR2(20),
  vendor_classify2 NVARCHAR2(20),
  qsa              NVARCHAR2(50),
  qsa_result       NVARCHAR2(50),
  qs_certificate   NVARCHAR2(500)
);

create table QEWEB_VENDOR_BASE_INFO_EX
(
  id            NUMBER(9) not null,
  vendor_id     NUMBER(9),
  org_id        NUMBER(9),
  ex_type       NUMBER(1),
  motor_factory NVARCHAR2(255),
  product_line  NVARCHAR2(255),
  car_model     NVARCHAR2(255),
  remark        NVARCHAR2(255)
);


create table QEWEB_VENDOR_CHANGEHIS
(
  id               NUMBER(9) not null,
  org_id           NUMBER(9) not null,
  vendor_name      NVARCHAR2(100),
  change_type      NUMBER(1),
  change_type_text NVARCHAR2(50),
  change_user      NVARCHAR2(50),
  change_time      TIMESTAMP(6),
  change_reason    NVARCHAR2(255)
);

create table QEWEB_VENDOR_DYNC_INFO
(
  id        NUMBER(9) not null,
  column_1  NVARCHAR2(50),
  column_2  NVARCHAR2(50),
  column_3  NVARCHAR2(50),
  column_4  NVARCHAR2(50),
  column_5  NVARCHAR2(50),
  column_6  NVARCHAR2(50),
  column_7  NVARCHAR2(50),
  column_8  NVARCHAR2(50),
  column_9  NVARCHAR2(50),
  column_10 NVARCHAR2(50),
  column_11 NVARCHAR2(50),
  column_12 NVARCHAR2(50),
  column_13 NVARCHAR2(50),
  column_14 NVARCHAR2(50),
  column_15 NVARCHAR2(50)
);


create table QEWEB_VENDOR_INVITE_EMAIL
(
  id               NUMBER(9),
  vendor_name      NVARCHAR2(100),
  vendor_email     NVARCHAR2(100),
  invite_name      NVARCHAR2(100),
  expiry_date      DATE,
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  is_check         NUMBER(1),
  is_register      NUMBER(1)
);

create table QEWEB_VENDOR_MATERIAL_REL
(
  id                 NUMBER(9) not null,
  org_id             NUMBER(9) not null,
  vendor_id          NUMBER(9),
  vendor_name        NVARCHAR2(255),
  material_id        NUMBER(9),
  material_name      NVARCHAR2(255),
  status             NUMBER(1),
  data_from          NVARCHAR2(255),
  abolished          NUMBER(1),
  create_time        TIMESTAMP(6),
  create_user_id     NUMBER(9),
  last_update_time   TIMESTAMP(6),
  update_user_id     NUMBER(9),
  create_user_name   NVARCHAR2(100),
  update_user_name   NVARCHAR2(100),
  leaf               NUMBER(1) default 0,
  importance         NUMBER(1) default 0,
  need_second_vendor NUMBER(1) default 0,
  car_model          NVARCHAR2(255)
);

create table QEWEB_VENDOR_MAT_SUPPLY_REL
(
  id                   NUMBER(9) not null,
  material_rel_id      NUMBER(9),
  vendor_id            NUMBER(9),
  vendor_name          NVARCHAR2(255),
  material_id          NUMBER(9),
  material_name        NVARCHAR2(255),
  bussiness_range_id   NUMBER(9),
  bussiness_range_name NVARCHAR2(255),
  bussiness_id         NUMBER(9),
  bussiness_name       NVARCHAR2(255),
  brand_id             NUMBER(9),
  brand_name           NVARCHAR2(255),
  product_line_id      NUMBER(9),
  product_line_name    NVARCHAR2(255),
  factory_id           NUMBER(9),
  factory_name         NVARCHAR2(255),
  ismain               NUMBER(1),
  supply_coefficient   FLOAT(20),
  abolished            NUMBER(1),
  create_time          TIMESTAMP(6),
  create_user_id       NUMBER(9),
  last_update_time     TIMESTAMP(6),
  update_user_id       NUMBER(9),
  create_user_name     NVARCHAR2(100),
  update_user_name     NVARCHAR2(100),
  leaf                 NUMBER(1) default 0,
  importance           NUMBER(1) default 0,
  need_second_vendor   NUMBER(1) default 0,
  org_id               NUMBER(9) not null
);


create table QEWEB_VENDOR_NAV_CFG
(
  id                 NUMBER(9) not null,
  org_id             NUMBER(9),
  nav_template_id    NUMBER(9),
  phase_id           NUMBER(9),
  survey_template_id NUMBER(9),
  survey_data_id     NUMBER(9),
  audit_status       NUMBER(1),
  approve_status     NUMBER(1),
  phase_name         NVARCHAR2(100),
  phase_code         NVARCHAR2(100),
  survey_code        NVARCHAR2(100),
  survey_name        NVARCHAR2(100),
  submit_status      NUMBER(1),
  remark             NVARCHAR2(255)
);

create table QEWEB_VENDOR_NAV_TEMPLATE
(
  id               NUMBER(9) not null,
  code             NVARCHAR2(100),
  name             NVARCHAR2(100),
  default_flag     NVARCHAR2(100),
  range_type       NUMBER(1),
  finish_status    NUMBER(1),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100)
);

create table QEWEB_VENDOR_PHASE
(
  id               NUMBER(9) not null,
  code             NVARCHAR2(100),
  name             NVARCHAR2(100),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  role_id          NUMBER(9),
  role_code        NVARCHAR2(100),
  role_name        NVARCHAR2(100)
);

create table QEWEB_VENDOR_PHASE_CFG
(
  id          NUMBER(9) not null,
  phase_id    NUMBER(9),
  phase_code  NVARCHAR2(50),
  phase_name  NVARCHAR2(50),
  phase_sn    NUMBER(1),
  vendor_id   NUMBER(9),
  org_id      NUMBER(9),
  template_id NUMBER(9)
);

create table QEWEB_VENDOR_PROPERTY_DICT
(
  id               NUMBER(9),
  code             NVARCHAR2(50),
  name             NVARCHAR2(50),
  label            NVARCHAR2(50),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100)
);

create table QEWEB_VENDOR_REPORT
(
  id               NUMBER(11) not null,
  report_name      NVARCHAR2(255),
  report_url       VARCHAR2(255),
  create_time      DATE,
  create_user_id   NUMBER(11),
  last_update_time DATE,
  update_user_id   NUMBER(11),
  create_user_name NVARCHAR2(255),
  update_user_name NVARCHAR2(255),
  abolished        NUMBER(1),
  orgid            NUMBER(11) not null
);

create table QEWEB_VENDOR_SUBJECT
(
  id      NUMBER(11) not null,
  title   NVARCHAR2(255),
  ques_id NUMBER(11)
);

create table QEWEB_VENDOR_SUPPLY_REL
(
  id                 NUMBER(9),
  org_id             NUMBER(9),
  material_id        NUMBER(9),
  material_type_id   NUMBER(9),
  supply_rate        NVARCHAR2(20),
  bussiness_id       NUMBER(9),
  bussiness_range_id NUMBER(9),
  product_line_id    NUMBER(9),
  abolished          NUMBER(1),
  create_time        TIMESTAMP(6),
  create_user_id     NUMBER(9),
  last_update_time   TIMESTAMP(6),
  update_user_id     NUMBER(9),
  create_user_name   NVARCHAR2(100),
  update_user_name   NVARCHAR2(100)
);

create table QEWEB_VENDOR_SURVEY_BASE
(
  id               NUMBER(9) not null,
  vendor_cfg_id    NUMBER(9),
  vendor_id        NUMBER(9),
  org_id           NUMBER(9),
  template_id      NVARCHAR2(50),
  template_path    NVARCHAR2(500),
  template_code    NVARCHAR2(50),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  versionno        NUMBER(3),
  current_version  NUMBER(1) default 0,
  audit_status     NUMBER(1),
  approve_status   NUMBER(1),
  submit_status    NUMBER(1),
  audit_reason     NVARCHAR2(255),
  audit_user       NVARCHAR2(255)
);

create table QEWEB_VENDOR_SURVEY_CFG
(
  id                 NUMBER(9) not null,
  org_id             NUMBER(9),
  nav_template_id    NUMBER(9),
  phase_id           NUMBER(9),
  survey_template_id NUMBER(9),
  survey_data_id     NUMBER(9),
  audit_status       NUMBER(1) default 0 not null,
  approve_status     NUMBER(1) default 0 not null,
  phase_name         NVARCHAR2(100),
  phase_code         NVARCHAR2(100),
  survey_code        NVARCHAR2(100),
  survey_name        NVARCHAR2(100),
  submit_status      NUMBER(1) default 0 not null,
  remark             NVARCHAR2(255),
  vendor_phasecfg_id NUMBER(9) not null,
  phase_sn           NUMBER(2) default 0,
  audit_reason       NVARCHAR2(255),
  audit_user         NVARCHAR2(255)
);

create table QEWEB_VENDOR_SURVEY_DATA
(
  id               NUMBER(9) not null,
  vendor_cfg_id    NUMBER(9),
  org_id           NUMBER(9),
  template_id      NUMBER(9),
  base_id          NVARCHAR2(50),
  data_type        NUMBER(1),
  vendor_id        NUMBER(9),
  col1             NVARCHAR2(50),
  col2             NVARCHAR2(50),
  col3             NVARCHAR2(50),
  col4             NVARCHAR2(50),
  col5             NVARCHAR2(50),
  col6             NVARCHAR2(50),
  col7             NVARCHAR2(50),
  col8             NVARCHAR2(50),
  col9             NVARCHAR2(50),
  col10            NVARCHAR2(50),
  col11            NVARCHAR2(50),
  col12            NVARCHAR2(50),
  col13            NVARCHAR2(50),
  col14            NVARCHAR2(50),
  col15            NVARCHAR2(50),
  col16            NVARCHAR2(50),
  col17            NVARCHAR2(50),
  col18            NVARCHAR2(50),
  col19            NVARCHAR2(50),
  col20            NVARCHAR2(50),
  col21            NVARCHAR2(50),
  col22            NVARCHAR2(50),
  col23            NVARCHAR2(50),
  col24            NVARCHAR2(50),
  col25            NVARCHAR2(50),
  col26            NVARCHAR2(50),
  col27            NVARCHAR2(50),
  col28            NVARCHAR2(50),
  col29            NVARCHAR2(50),
  col30            NVARCHAR2(50),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  ct_id            NVARCHAR2(50),
  ct_code          NVARCHAR2(50),
  ct_name          NVARCHAR2(50),
  fixed            NUMBER(1) default 0 not null
);

create table QEWEB_VENDOR_SURVEY_DATA_HIS
(
  id             NUMBER(9),
  vendor_cfg_id  NUMBER(9),
  org_id         NUMBER(9),
  template_id    NUMBER(9),
  template_path  NVARCHAR2(50),
  data_type      NUMBER(1),
  container_code NVARCHAR2(50),
  column_1       NVARCHAR2(50),
  column_2       NVARCHAR2(50),
  column_3       NVARCHAR2(50),
  column_4       NVARCHAR2(50),
  column_5       NVARCHAR2(50),
  column_6       NVARCHAR2(50),
  column_7       NVARCHAR2(50),
  column_8       NVARCHAR2(50),
  column_9       NVARCHAR2(50),
  column_10      NVARCHAR2(50),
  column_11      NVARCHAR2(50),
  column_12      NVARCHAR2(50),
  column_13      NVARCHAR2(50),
  column_14      NVARCHAR2(50),
  column_15      NVARCHAR2(50),
  column_16      NVARCHAR2(50),
  column_17      NVARCHAR2(50),
  column_18      NVARCHAR2(50),
  column_19      NVARCHAR2(50),
  column_20      NVARCHAR2(50),
  column_21      NVARCHAR2(50),
  column_22      NVARCHAR2(50),
  column_23      NVARCHAR2(50),
  column_24      NVARCHAR2(50),
  column_25      NVARCHAR2(50),
  column_26      NVARCHAR2(50),
  column_27      NVARCHAR2(50),
  column_28      NVARCHAR2(50),
  column_29      NVARCHAR2(50),
  column_30      NVARCHAR2(50)
);

create table QEWEB_VENDOR_SURVEY_TEMPLATE
(
  id               NUMBER(9) not null,
  code             NVARCHAR2(100),
  name             NVARCHAR2(100),
  template_type    NUMBER(1),
  bean_id          NVARCHAR2(100),
  path             NVARCHAR2(500),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100),
  file_name        NVARCHAR2(100)
);

create table QEWEB_VENDOR_TEMPLATE_PHASE
(
  id               NUMBER(9) not null,
  template_id      NUMBER(9),
  phase_id         NUMBER(9),
  code             NVARCHAR2(100),
  name             NVARCHAR2(100),
  phase_sn         NUMBER(2),
  remark           NVARCHAR2(255),
  abolished        NUMBER(1),
  create_time      TIMESTAMP(6),
  create_user_id   NUMBER(9),
  last_update_time TIMESTAMP(6),
  update_user_id   NUMBER(9),
  create_user_name NVARCHAR2(100),
  update_user_name NVARCHAR2(100)
);

create table QEWEB_VENDOR_TEMPLATE_RANGE
(
  id                 NUMBER(9) not null,
  template_id        NUMBER(9),
  material_id        NUMBER(9),
  material_code      NVARCHAR2(100),
  material_name      NVARCHAR2(100),
  material_type_code NVARCHAR2(100),
  material_type_name NVARCHAR2(100),
  material_type_id   NUMBER(9),
  vendor_id          NUMBER(9),
  vendor_code        NVARCHAR2(100),
  vendor_name        NVARCHAR2(100),
  remark             NVARCHAR2(255),
  abolished          NUMBER(1),
  create_time        TIMESTAMP(6),
  create_user_id     NUMBER(9),
  last_update_time   TIMESTAMP(6),
  update_user_id     NUMBER(9),
  create_user_name   NVARCHAR2(100),
  update_user_name   NVARCHAR2(100)
);

create table QEWEB_VENDOR_TEMPLATE_SURVEY
(
  id                 NUMBER(9) not null,
  template_phase_id  NUMBER(9),
  survey_template_id NUMBER(9),
  phase_id           NUMBER(9),
  vendor_template_id NUMBER(9),
  phase_code         NVARCHAR2(100),
  phase_name         NVARCHAR2(100),
  phase_sn           NUMBER(2),
  survey_name        NVARCHAR2(100),
  remark             NVARCHAR2(255),
  abolished          NUMBER(1),
  create_time        TIMESTAMP(6),
  create_user_id     NUMBER(9),
  last_update_time   TIMESTAMP(6),
  update_user_id     NUMBER(9),
  create_user_name   NVARCHAR2(100),
  update_user_name   NVARCHAR2(100),
  survey_code        NVARCHAR2(100)
);
CREATE OR REPLACE VIEW "VIEW_MATERIAL_SUPPLY_REL" AS 
SELECT
V.ID AS V_ID,
V.CODE AS V_CODE,
V.NAME AS V_NAME,
V.SHORT_NAME AS V_SHORT_NAME,
V.PROPERTY AS V_PROPERTY,
V.COUNTRY AS V_COUNTRY,
V.PROVINCE AS V_PROVINCE,
V.CITY AS V_CITY,
V.ADDRESS AS V_ADDRESS,
V.MATERIAL_ID AS V_MATERIAL_ID,
V.MATERIAL_TYPE_ID AS V_MATERIAL_TYPE_ID,
V.PRODUCT_LINE AS V_PRODUCT_LINE,
V.LAST_YEAR_INCOME AS V_LAST_YEAR_INCOME,
V.EMPLOYEE_AMOUNT AS V_EMPLOYEE_AMOUNT,
V.ABOLISHED AS V_ABOLISHED,
V.CREATE_TIME AS V_CREATE_TIME,
V.CREATE_USER_ID AS V_CREATE_USER_ID,
V.LAST_UPDATE_TIME AS V_LAST_UPDATE_TIME,
V.UPDATE_USER_ID AS V_UPDATE_USER_ID,
V.CREATE_USER_NAME AS V_CREATE_USER_NAME,
V.UPDATE_USER_NAME AS V_UPDATE_USER_NAME,
V.LEGAL_PERSON AS V_LEGAL_PERSON,
V.STOCK_SHARE AS V_STOCK_SHARE,
V.MAIN_PRODUCT AS V_MAIN_PRODUCT,
V.ORG_ID AS V_ORG_ID,
V.PHASE_ID AS V_PHASE_ID,
V.TEMPLATE_ID AS V_TEMPLATE_ID,
V.IPO AS V_IPO,
V.WEB_ADDR AS V_WEB_ADDR,
V.TAX_ID AS V_TAX_ID,
V.OWNERSHIP AS V_OWNERSHIP,
V.BANK_NAME AS V_BANK_NAME,
V.MAINBU AS V_MAINBU,
V.REG_CAPITAL AS V_REG_CAPITAL,
V.TOTAL_CAPITAL AS V_TOTAL_CAPITAL,
V.WORKING_CAPITAL AS V_WORKING_CAPITAL,
V.FLOOR_AREA AS V_FLOOR_AREA,
V.VENDOR_TYPE AS V_VENDOR_TYPE,
V.PHASE_CFG_ID AS V_PHASE_CFG_ID,
V.PHASE_SN AS V_PHASE_SN,
V.DISTRICT AS V_DISTRICT,
V.COUNTRY_TEXT AS V_COUNTRY_TEXT,
V.PROVINCE_TEXT AS V_PROVINCE_TEXT,
V.CITY_TEXT AS V_CITY_TEXT,
V.DISTRICT_TEXT AS V_DISTRICT_TEXT,
V.PRODUCT_POWER AS V_PRODUCT_POWER,
V.VERSIONNO AS V_VERSIONNO,
V.CURRENT_VERSION AS V_CURRENT_VERSION,
V.AUDIT_STATUS AS V_AUDIT_STATUS,
V.APPROVE_STATUS AS V_APPROVE_STATUS,
V.SUBMIT_STATUS AS V_SUBMIT_STATUS,
V.VENDOR_LEVEL AS V_VENDOR_LEVEL,
V.VENDOR_CLASSIFY AS V_VENDOR_CLASSIFY,
V.VENDOR_CLASSIFY2 AS V_VENDOR_CLASSIFY2,
V.QSA AS V_QSA,
V.QSA_RESULT AS V_QSA_RESULT,
V.QS_CERTIFICATE AS V_QS_CERTIFICATE,
MSR.ID AS MSR_ID,
MSR.MATERIAL_REL_ID AS MSR_MATERIAL_REL_ID,
MSR.VENDOR_ID AS MSR_VENDOR_ID,
MSR.VENDOR_NAME AS MSR_VENDOR_NAME,
MSR.MATERIAL_ID AS MSR_MATERIAL_ID,
MSR.MATERIAL_NAME AS MSR_MATERIAL_NAME,
MSR.BUSSINESS_RANGE_ID AS MSR_BUSSINESS_RANGE_ID,
MSR.BUSSINESS_RANGE_NAME AS MSR_BUSSINESS_RANGE_NAME,
MSR.BUSSINESS_ID AS MSR_BUSSINESS_ID,
MSR.BUSSINESS_NAME AS MSR_BUSSINESS_NAME,
MSR.BRAND_ID AS MSR_BRAND_ID,
MSR.BRAND_NAME AS MSR_BRAND_NAME,
MSR.PRODUCT_LINE_ID AS MSR_PRODUCT_LINE_ID,
MSR.PRODUCT_LINE_NAME AS MSR_PRODUCT_LINE_NAME,
MSR.FACTORY_ID AS MSR_FACTORY_ID,
MSR.FACTORY_NAME AS MSR_FACTORY_NAME,
MSR.ISMAIN AS MSR_ISMAIN,
MSR.SUPPLY_COEFFICIENT AS MSR_SUPPLY_COEFFICIENT,
MSR.ABOLISHED AS MSR_ABOLISHED,
MSR.CREATE_TIME AS MSR_CREATE_TIME,
MSR.CREATE_USER_ID AS MSR_CREATE_USER_ID,
MSR.LAST_UPDATE_TIME AS MSR_LAST_UPDATE_TIME,
MSR.UPDATE_USER_ID AS MSR_UPDATE_USER_ID,
MSR.CREATE_USER_NAME AS MSR_CREATE_USER_NAME,
MSR.UPDATE_USER_NAME AS MSR_UPDATE_USER_NAME,
MSR.LEAF AS MSR_LEAF,
MSR.IMPORTANCE AS MSR_IMPORTANCE,
MSR.NEED_SECOND_VENDOR AS MSR_NEED_SECOND_VENDOR,
MSR.ORG_ID AS MSR_ORG_ID,
MAT.ID AS MAT_ID,
MAT.CODE AS MAT_CODE,
MAT.NAME AS MAT_NAME,
MAT.SPECIFICATION AS MAT_SPECIFICATION,
MAT.DESCRIBE AS MAT_DESCRIBE,
MAT.UNIT AS MAT_UNIT,
MAT.UNIT_AMOUNT AS MAT_UNIT_AMOUNT,
MAT.ENABLE_STATUS AS MAT_ENABLE_STATUS,
MAT.TECHNICIAN AS MAT_TECHNICIAN,
MAT.PARTS_CODE AS MAT_PARTS_CODE,
MAT.PARTS_NAME AS MAT_PARTS_NAME,
MAT.MATERIAL_TYPE_ID AS MAT_MATERIAL_TYPE_ID,
MAT.REMARK AS MAT_REMARK,
MAT.ABOLISHED AS MAT_ABOLISHED,
MAT.CREATE_TIME AS MAT_CREATE_TIME,
MAT.CREATE_USER_ID AS MAT_CREATE_USER_ID,
MAT.LAST_UPDATE_TIME AS MAT_LAST_UPDATE_TIME,
MAT.UPDATE_USER_ID AS MAT_UPDATE_USER_ID,
MAT.CREATE_USER_NAME AS MAT_CREATE_USER_NAME,
MAT.UPDATE_USER_NAME AS MAT_UPDATE_USER_NAME,
MAT.PIC_STATUS AS MAT_PIC_STATUS,
MAT.CATEGORY_STATUS AS MAT_CATEGORY_STATUS,
MAT.VESION AS MAT_VESION,
MAT.EDITION AS MAT_EDITION,
MAT.REFERENCE_NUM AS MAT_REFERENCE_NUM,
MAT.MAPPABLE_UNIT AS MAT_MAPPABLE_UNIT,
MAT.WEIGHT AS MAT_WEIGHT,
MAT.GRADE AS MAT_GRADE,
MAT.PARTS_TYPE AS MAT_PARTS_TYPE,
MATT.LEVEL_LAYER AS MATT_LEVEL_LAYER,
MATT.NAME AS MATT_NAME,
VP.NAME AS VP_NAME,
VP.CODE AS VP_CODE
FROM
FOTON.QEWEB_VENDOR_BASE_INFO V
INNER JOIN FOTON.QEWEB_VENDOR_MAT_SUPPLY_REL MSR ON V.ID = MSR.VENDOR_ID
INNER JOIN FOTON.QEWEB_MATERIAL MAT ON MSR.MATERIAL_ID = MAT.ID
INNER JOIN FOTON.QEWEB_MATERIAL_TYPE MATT ON MAT.MATERIAL_TYPE_ID = MATT.ID
INNER JOIN FOTON.QEWEB_VENDOR_PHASE VP ON V.PHASE_ID = VP.ID;


CREATE OR REPLACE VIEW "VIEW_VENDOR" AS 
SELECT
V.ID AS V_ID,
VP.ID AS VP_ID,
VP.NAME AS VP_NAME,
VP.CODE AS VP_CODE,
V.CODE AS V_CODE,
V.NAME AS V_NAME,
V.SHORT_NAME AS V_SHORT_NAME,
V.PROPERTY AS V_PROPERTY,
V.COUNTRY AS V_COUNTRY,
V.PROVINCE AS V_PROVINCE,
V.CITY AS V_CITY,
V.ADDRESS AS V_ADDRESS,
V.MATERIAL_ID AS V_MATERIAL_ID,
V.MATERIAL_TYPE_ID AS V_MATERIAL_TYPE_ID,
V.PRODUCT_LINE AS V_PRODUCT_LINE,
V.MAIN_PRODUCT AS V_MAIN_PRODUCT,
V.ORG_ID AS V_ORG_ID,
V.PHASE_ID AS V_PHASE_ID,
V.COUNTRY_TEXT AS V_COUNTRY_TEXT,
V.PROVINCE_TEXT AS V_PROVINCE_TEXT,
V.VENDOR_TYPE AS V_VENDOR_TYPE,
V.CITY_TEXT AS V_CITY_TEXT,
V.DISTRICT_TEXT AS V_DISTRICT_TEXT,
V.PRODUCT_POWER AS V_PRODUCT_POWER,
V.CURRENT_VERSION AS V_CURRENT_VERSION,
V.VENDOR_LEVEL AS V_VENDOR_LEVEL,
V.VENDOR_CLASSIFY AS V_VENDOR_CLASSIFY,
V.VENDOR_CLASSIFY2 AS V_VENDOR_CLASSIFY2
FROM
QEWEB_VENDOR_BASE_INFO V
INNER JOIN QEWEB_VENDOR_PHASE VP ON V.PHASE_ID = VP.ID;

