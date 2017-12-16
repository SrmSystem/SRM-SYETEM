/**更新企业性质类型为nvarchar2*/
update QEWEB_VENDOR_BASE_INFO set property='';
alter table QEWEB_VENDOR_BASE_INFO modify property nvarchar2(64);