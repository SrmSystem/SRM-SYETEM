drop table if exists qeweb_answer;
drop table if exists qeweb_user;
drop table if exists qeweb_organization;
drop table if exists qeweb_user_org;
drop table if exists qeweb_area;
drop table if exists qeweb_brand;
drop table if exists qeweb_bussiness_domain;
drop table if exists qeweb_bussiness_range;
drop table if exists qeweb_bus_domain_role;
drop table if exists qeweb_bus_domain_view;
drop table if exists qeweb_column_setting;
drop table if exists qeweb_comment;
drop table if exists qeweb_company_bussiness;
drop table if exists qeweb_data_permission;
drop table if exists qeweb_dynamicdata;
drop table if exists qeweb_dynamicdata_scene;
drop table if exists qeweb_factory;
drop table if exists qeweb_factory_brand;
drop table if exists qeweb_mailset;
drop table if exists qeweb_material;
drop table if exists qeweb_material_type;
drop table if exists qeweb_message;
drop table if exists qeweb_notice;
drop table if exists qeweb_product_line;
drop table if exists qeweb_questionnaire;
drop table if exists qeweb_role;
drop table if exists qeweb_role_data;
drop table if exists qeweb_role_organization;
drop table if exists qeweb_role_user;
drop table if exists qeweb_role_view;
drop table if exists qeweb_subject;
drop table if exists qeweb_serial_number;
drop table if exists qeweb_status_dict;
drop table if exists qeweb_storehouse;
drop table if exists qeweb_view;

create table qeweb_answer (
	id bigint generated by default as identity,
	title nvarchar2(255) null ,
	type number(1) null ,
	subject_id number(11) null ,
	choice_number number(11) null,
	primary key (id)
);

create table qeweb_user (
	id bigint generated by default as identity,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	salt varchar(64) not null,
	password varchar(255) not null,
	roles varchar(255) not null,
	register_date timestamp not null,
	company_id bigint,
	department_id bigint,
	mobile varchar(11),
	email varchar(100),
	enabled_status int(1),
	
	create_time timestamp,
	create_user_id bigint,
	last_update_time timestamp,
	update_user_id bigint,
	create_user_name varchar(255),
	update_user_name varchar(255),
	abolished int(1),
	primary key (id)
);

create table qeweb_organization(
	id bigint generated by default as identity,
	code varchar(64) not null unique,
	mid_code varchar(64),
	name varchar(64) not null,
	legal_person varchar(255),
	org_type int(1) not null,
	role_type int(1) not null,
	register_time timestamp,
	top_parent_id bigint,
	parent_id bigint,
	level_describe varchar(255),
	email varchar(100),
	phone varchar(100),
	
	
	create_time timestamp,
	create_user_id bigint,
	last_update_time timestamp,
	update_user_id bigint,
	create_user_name varchar(255),
	update_user_name varchar(255),
	abolished int(1),
	active_status int(1),
	enable_status int(1),
	confirm_status int(1),
	primary key (id)

);

create table qeweb_user_org(
	id bigint generated by default as identity,
	user_id bigint not null,
	org_id bigint not null,
	org_type int(1) not null,
	org_role_type int(1) not null,
	org_name varchar(255) not null,
	org_code varchar(100),
	user_name varchar(100),
	primary key (id)
);

create table qeweb_area
(
  id         number(9) not null,
  name       nvarchar2(100),
  area_level number(2),
  parent_id  number(9)
);

create table qeweb_brand
(
  id               number(9),
  code             nvarchar2(50),
  name             nvarchar2(50),
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100)
);

create table qeweb_bussiness_domain
(
  id               number(9),
  code             nvarchar2(100),
  name             nvarchar2(100),
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100)
);

create table qeweb_bussiness_range
(
  id               number(9) not null,
  code             nvarchar2(50) not null,
  name             nvarchar2(50) not null,
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100),
  bussiness_type   number(1),
  parent_id        number(9)
);

create table qeweb_bus_domain_role
(
  id                  number(9),
  bussiness_domain_id number(9),
  role_id             number(9),
  remark              nvarchar2(255)
);

create table qeweb_bus_domain_view
(
  id                  number(9),
  bussiness_domain_id number(9),
  view_id             number(9),
  remark              nvarchar2(255)
);

create table qeweb_column_setting
(
  id                number not null,
  user_id           number not null,
  path              varchar2(500) not null,
  table_id          varchar2(100) not null,
  sort_width_string varchar2(500) not null,
  create_time       date,
  create_user_id    number(11),
  last_update_time  date,
  update_user_id    number(11),
  create_user_name  nvarchar2(255),
  update_user_name  nvarchar2(255),
  abolished         number(1)
);

create table qeweb_comment
(
  id               number(11) not null,
  notice_id        number(11),
  content          nvarchar2(255),
  comment_type     number(1),
  create_time      date,
  create_user_id   number(11),
  last_update_time date,
  update_user_id   number(11),
  create_user_name nvarchar2(255),
  update_user_name nvarchar2(255),
  abolished        number(1)
);

create table qeweb_company_bussiness
(
  id                   number(9),
  code                 nvarchar2(50),
  name                 nvarchar2(50),
  remark               nvarchar2(255),
  abolished            number(1),
  create_time          timestamp(6),
  create_user_id       number(9),
  last_update_time     timestamp(6),
  update_user_id       number(9),
  create_user_name     nvarchar2(100),
  update_user_name     nvarchar2(100),
  bussiness_range_id   number(9),
  bussiness_range_name nvarchar2(50),
  bussiness_range_code nvarchar2(50)
);
 
create table qeweb_data_permission
(
  id               number(9) not null,
  bean_id          nvarchar2(100),
  field_name       nvarchar2(100),
  sql_rule         nvarchar2(100),
  rule             nvarchar2(100),
  org_type         nvarchar2(100),
  org_ids          nvarchar2(1000),
  user_ids         nvarchar2(1000),
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100)
);

create table qeweb_dynamicdata
(
  id               number(11) not null,
  bean_id          nvarchar2(64) not null,
  object_name      nvarchar2(64),
  enable		   number(1) not null,
  remark           nvarchar2(64),
  create_user_id   number(11),
  create_user_name nvarchar2(255),
  create_time      date,
  update_user_id   number(11),
  update_user_name nvarchar2(255),
  last_update_time date,
  abolished        number(1) not null
);

create table qeweb_dynamicdata_scene
(
  id               number(11) not null,
  data_ex_id       number(11),
  col_code         nvarchar2(64),
  name             nvarchar2(64),
  range            nvarchar2(64),
  type             nvarchar2(64),
  way              nvarchar2(64),
  status_key	   nvarchar2(64),
  filter           number(1),
  required         number(1),
  is_show          number(1),
  create_user_id   number(11),
  create_user_name nvarchar2(255),
  create_time      date,
  update_user_id   number(11),
  update_user_name nvarchar2(255),
  last_update_time date,
  abolished        number(1) not null
);

create table qeweb_factory
(
  id               number(9),
  code             nvarchar2(50),
  name             nvarchar2(50),
  brand_id         number(9),
  org_id           number(9),
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100)
);

create table qeweb_factory_brand
(
  id         number(9) not null,
  factory_id number(9),
  brand_id   number(9),
  remark     varchar2(255)
);

create table qeweb_mailset
(
  id               number(9) not null,
  mail_template_id number(3),
  mail_address     nvarchar2(50),
  server_address   nvarchar2(50),
  account          nvarchar2(50),
  password         nvarchar2(50),
  mail_content     nvarchar2(1000),
  signature        nvarchar2(1000),
  abolished        number(1),
  create_user_id   number(9),
  create_user_name nvarchar2(50),
  update_user_id   number(9),
  update_user_name nvarchar2(50),
  create_time      timestamp(6),
  last_update_time timestamp(6)
);

create table qeweb_material
(
  id               number(9) not null,
  code             nvarchar2(50),
  name             nvarchar2(150),
  specification    nvarchar2(50),
  describe         nvarchar2(255),
  unit             nvarchar2(50),
  unit_amount      nvarchar2(50),
  enable_status    number(1) default 0,
  technician       nvarchar2(50),
  parts_code       nvarchar2(50),
  parts_name       nvarchar2(50),
  material_type_id number(9) default 0,
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100),
  pic_status       nvarchar2(2),
  category_status  number(1) default 0,
  vesion           nvarchar2(54),
  edition          nvarchar2(54),
  mappableunit     nvarchar2(54),
  weight           nvarchar2(54),
  grade            nvarchar2(54),
  reference_num    nvarchar2(54),
  mappable_unit    nvarchar2(54),
  parts_type       nvarchar2(10)
);

create table qeweb_material_type
(
  id                 number(9),
  code               nvarchar2(50),
  name               nvarchar2(50),
  parent_id          number(9) default 0,
  parent_top_id      number(9) default 0,
  level_info         nvarchar2(50),
  level_describe     nvarchar2(50),
  remark             nvarchar2(255),
  abolished          number(1),
  create_time        timestamp(6),
  create_user_id     number(9),
  last_update_time   timestamp(6),
  update_user_id     number(9),
  create_user_name   nvarchar2(100),
  update_user_name   nvarchar2(100),
  leaf               number(1) default 0,
  importance         number(1) default 0,
  need_second_vendor number(1) default 0,
  level_layer        number(2) default 0,
  col1               nvarchar2(100)
);

create table qeweb_message
(
  id               number(11) not null,
  title            nvarchar2(64) not null,
  msg              nvarchar2(254),
  module_id        number(11),
  from_user_id     number(11),
  to_user_id       number(11),
  is_read          number(1),
  create_user_id   number(11),
  create_user_name nvarchar2(255),
  create_time      date,
  update_user_id   number(11),
  update_user_name nvarchar2(255),
  last_update_time date,
  abolished        number(1) not null
);

create table qeweb_notice
(
  id               number(11) not null,
  title            nvarchar2(255),
  content          clob,
  create_time      date,
  create_user_id   number(11),
  last_update_time date,
  update_user_id   number(11),
  create_user_name nvarchar2(255),
  update_user_name nvarchar2(255),
  abolished        number(1),
  comment_power    number(1),
  valid_start_time date not null,
  valid_end_time   date not null
);

create table qeweb_product_line
(
  id               number(9),
  code             nvarchar2(50),
  name             nvarchar2(50),
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100),
  brand_name       nvarchar2(50),
  brand_code       nvarchar2(50),
  brand_id         number(9)
);

create table qeweb_questionnaire (
	id number(11) not null ,
	title nvarchar2(255) null ,
	ques_html clob null ,
	release_time timestamp(0)  null ,
	releaseer_user_name nvarchar2(255) null ,
	end_time timestamp(0)  null ,
	status number(1) null ,
	create_time date null ,
	create_user_id number(11) null ,
	last_update_time date null ,
	update_user_id number(11) null ,
	create_user_name nvarchar2(255) null ,
	update_user_name nvarchar2(255) null ,
	abolished number(1) null ,
	look_number number(11) null ,
	answer_number number(11) null 
);

create table qeweb_role
(
  code             nvarchar2(100) not null,
  name             nvarchar2(100) not null,
  remark           nvarchar2(255),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100),
  role_type        number(1),
  id               number(9) not null
);

create table qeweb_role_data
(
  role_id number(9),
  data_id number(9),
  remark  nvarchar2(255)
);

create table qeweb_role_organization
(
  id      number(9),
  role_id number(9),
  org_id  number(9),
  remark  nvarchar2(255)
);

create table qeweb_role_user
(
  id      number(9) not null,
  role_id number(9),
  user_id number(9),
  remark  nvarchar2(255)
);

create table qeweb_role_view
(
  id         number(9) not null,
  role_id    number(9),
  view_id    number(9),
  remark     nvarchar2(255),
  view_type  number(1),
  operations nvarchar2(255),
  view_pid   number(9)
);

create table qeweb_subject (
	id number(11) not null ,
	title nvarchar2(255) null ,
	ques_id number(11) null 
);

create table qeweb_status_dict
(
  id bigint generated by default as identity,
  status_name  varchar(100),
  status_value bigint,
  status_text  varchar(100),
  status_icon  varchar(100),
  status_type  varchar(100),
  primary key (id)
);

create table qeweb_serial_number
(
  id               number(11) not null,
  prefix           nvarchar2(36) not null,
  data_string      nvarchar2(36) not null,
  start_number     nvarchar2(36),
  date_time_string nvarchar2(36),
  repeat_cycle     nvarchar2(10),
  is_verify        number(1) not null,
  create_time      date,
  last_update_time date,
  create_user_id   number(11),
  update_user_id   number(11),
  create_user_name nvarchar2(64),
  update_user_name nvarchar2(64),
  abolished        number(1),
  remark           nvarchar2(128),
  serial_key       nvarchar2(64) default 0 not null
);

create table qeweb_storehouse
(
  id               number(11) not null,
  code             nvarchar2(64) not null,
  name             nvarchar2(64),
  create_user_id   number(11),
  create_user_name nvarchar2(255),
  create_time      date,
  update_user_id   number(11),
  update_user_name nvarchar2(255),
  last_update_time date,
  abolished        number(1) not null,
  remark           nvarchar2(64)
);

create table qeweb_view
(
  id               number(9) not null,
  view_code        nvarchar2(100),
  view_name        nvarchar2(100),
  view_icon        nvarchar2(100),
  view_url         nvarchar2(500),
  view_type        number(1),
  parent_id        number(9) default 0,
  is_leaf          number(1),
  menu_sn          number(3),
  remark           nvarchar2(100),
  abolished        number(1),
  create_time      timestamp(6),
  create_user_id   number(9),
  last_update_time timestamp(6),
  update_user_id   number(9),
  create_user_name nvarchar2(100),
  update_user_name nvarchar2(100)
);
