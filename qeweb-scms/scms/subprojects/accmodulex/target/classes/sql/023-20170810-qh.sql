------删除 部分权限------
delete from QEWEB_ROLE_DATA_CFG where id = 2;
delete from QEWEB_ROLE_DATA_CFG where id = 5;
delete from QEWEB_ROLE_DATA_CFG where id = 6;

-----添加公司 和 工厂的权限
INSERT INTO "QEWEB_ROLE_DATA_CFG" VALUES ('9', '9', '工厂权限', 'factoryEntity', 'select id,code,name from FactoryEntity where abolished = 0', null);
INSERT INTO "QEWEB_ROLE_DATA_CFG" VALUES ('8', '8', '公司权限', 'companyEntity', 'select id,code,name from  CompanyEntity where abolished = 0', null);