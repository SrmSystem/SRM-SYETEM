insert into QEWEB_ORGANIZATION (ID, CODE, MID_CODE, NAME, LEGAL_PERSON, ORG_TYPE, ROLE_TYPE, REGISTER_TIME, TOP_PARENT_ID, PARENT_ID, LEVEL_DESCRIBE, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME, ABOLISHED, EMAIL, PHONE, ACTIVE_STATUS, ENABLE_STATUS, CONFIRM_STATUS)
values (1, '001', '001', '快维', null, 0, 0, to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), null, null, null, to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), 1, to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), 1, 'admin', 'admin', 0, null, null, null, null, 0);

insert into QEWEB_USER (ID, LOGIN_NAME, NAME, SALT, PASSWORD, ROLES, REGISTER_DATE, COMPANY_ID, DEPARTMENT_ID, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME, ABOLISHED, MOBILE, EMAIL, ENABLED_STATUS)
values (1, 'admin', 'Admin', '7efbd59d9741d34f', '691b14d79bf0fa2215f155235df5e670b64394cc', 'admin', to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), 1, 2, to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), 1, to_date('20-04-2015 14:09:05', 'dd-mm-yyyy hh24:mi:ss'), 1, 'admin', 'admin', 0, null, null, null);


insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1004, 'org', '组织管理', null, 'manager/admin/org/', 0, 1002, 0, 2, null, 0, '28-4月 -15 08.17.31.000000 上午', 1, '28-4月 -15 08.17.31.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1060, 'mailSet', '邮箱设置', null, 'manager/basedata/mailSet/', 0, 1020, 0, 7, null, 0, '30-4月 -15 09.10.28.017000 上午', 1, '30-4月 -15 09.10.28.017000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1003, 'user', '用户管理', null, 'manager/admin/user/', 0, 1002, 0, 1, null, 0, '28-4月 -15 12.16.24.000000 上午', 1, '28-4月 -15 12.16.24.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1903, 'account_payable', '应付款台账', null, 'manager/account/payable/', 0, 1900, 0, 3, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1009, 'goods_request', '要货管理', null, 'manager/order/goodsrequest/', 0, 1006, 0, 5, null, 0, '27-4月 -15 04.20.25.000000 下午', 1, '27-4月 -15 04.20.25.000000 下午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1, 'sys', '系统管理', null, null, 0, 0, 1, 2, '1', 0, '15-9月 -13 05.03.00.000000 下午', 1, '15-9月 -13 05.03.00.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1901, 'account_setting', '物料结算配置', null, 'manager/account/setting/', 0, 1900, 0, 1, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1002, 'right', '权限管理', null, null, 0, 0, 0, 4, null, null, '27-4月 -15 12.09.19.000000 上午', 1, '27-4月 -15 12.09.19.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1070, 'area', '区域管理', null, 'manager/basedata/area/', 0, 1020, 0, 8, null, 0, '02-6月 -15 01.38.57.000000 下午', 1, '02-6月 -15 01.39.06.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1018, 'inventory', '库存管理', null, null, 0, 0, 0, 18, null, 0, '28-4月 -15 04.20.25.453000 下午', 1, '28-4月 -15 04.20.25.453000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1907, 'online_invoice', '开票列表(上线)', null, 'manager/account/bill/online/', 0, 1900, 0, 7, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1207, 'vendor_online_invoice', '开票列表(上线)', null, 'manager/account/bill/vendor/online/', 0, 1200, 0, 7, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1905, 'in_invoice', '开票列表(入库)', null, 'manager/account/bill/in/', 0, 1900, 0, 5, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1202, 'vendor_in_invoice', '开票列表(入库)', null, 'manager/account/bill/vendor/in/', 0, 1200, 0, 2, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1054, 'bussinessRange', '公司业务范围', null, 'manager/basedata/bussinessRange/bussinessRangeList/', 0, 1020, 0, 2, null, 0, '29-4月 -15 04.47.44.000000 下午', 1, '29-4月 -15 04.47.44.000000 下午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1181, 'vendor_phase', '供应商阶段管理', null, 'manager/vendor/vendorPhase/', 0, 1180, 0, 6, null, 0, '06-5月 -15 05.41.12.000000 上午', 1, '06-5月 -15 05.41.12.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1050, 'factory', '工厂管理', null, 'manager/basedata/factory/', 0, 1020, 0, 6, null, 0, '30-4月 -15 08.37.13.340000 上午', 1, '30-4月 -15 08.37.13.340000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (5340, 'vendorCount', '各维度供应商数量统计', null, 'manager/vendor/statistics', 0, 1180, 0, 11, null, 0, '06-7月 -15 02.08.21.000000 上午', 1, '06-7月 -15 02.08.21.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1061, 'dynamicData', '动态数据管理', null, 'manager/basedata/dynamicData/', 0, 1020, 0, 9, null, 0, '04-6月 -15 09.10.28.017000 上午', 1, '04-6月 -15 09.10.28.017000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1740, 'survey', '调查表模版管理', null, 'manager/vendor/vendorSurveyTemplate/', 0, 1180, 0, 7, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1203, 'vendor_online', '待开票清单(上线)', null, 'manager/account/online/', 0, 1200, 0, 3, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1201, 'vendor_instorage', '待开票清单(入库)', null, 'manager/account/instorage/', 0, 1200, 0, 1, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (2, 'menu', '菜单管理', null, 'manager/admin/menu/goMenu/', 0, 1, 1, 2, '1', 0, '16-9月 -13 09.03.00.000000 上午', 1, '16-9月 -13 09.03.00.000000 上午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1006, 'purchase', '采购协同', null, null, 0, 0, 0, 6, null, 0, '28-4月 -15 04.19.11.900000 下午', 1, '28-4月 -15 04.19.11.900000 下午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1007, 'purchase_plan', '采购计划管理(fast)', null, 'manager/order/purchaseplan/', 0, 1006, 0, 1, null, 0, '28-4月 -15 04.19.48.283000 下午', 1, '28-4月 -15 04.19.48.283000 下午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1200, 'vendor_account', '财务对账(fast供方)', null, null, 0, 0, 0, 30, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1900, 'account', '财务对账(fast采方)', null, null, 0, 0, 0, 20, null, 0, '10-5月 -15 07.19.37.000000 下午', 1, '10-5月 -15 07.19.37.000000 下午', 1, 'admin', 'admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1005, 'department', '部门管理', null, 'manager/admin/org/goDepartment/', 0, 1002, 0, 3, null, 0, '28-4月 -15 08.17.53.000000 上午', 1, '28-4月 -15 08.17.53.000000 上午', 1, 'Admin', 'Admin');

insert into QEWEB_VIEW (ID, VIEW_CODE, VIEW_NAME, VIEW_ICON, VIEW_URL, VIEW_TYPE, PARENT_ID, IS_LEAF, MENU_SN, REMARK, ABOLISHED, CREATE_TIME, CREATE_USER_ID, LAST_UPDATE_TIME, UPDATE_USER_ID, CREATE_USER_NAME, UPDATE_USER_NAME)
values (1019, 'inventory_list', 'VMI库存管理', null, 'manager/inventory/vmi/', 0, 1018, 0, 1, null, 0, '28-4月 -15 04.20.25.453000 下午', 1, '28-4月 -15 04.20.25.453000 下午', 1, 'admin', 'admin');

commit;