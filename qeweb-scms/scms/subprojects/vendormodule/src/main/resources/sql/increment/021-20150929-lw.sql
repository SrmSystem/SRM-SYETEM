ALTER TABLE QEWEB_VENDOR_BASE_INFO add MAINBU_1 NVARCHAR2(200);
update QEWEB_VENDOR_BASE_INFO set MAINBU_1 = MAINBU;
update QEWEB_VENDOR_BASE_INFO set MAINBU = null;
ALTER TABLE QEWEB_VENDOR_BASE_INFO MODIFY ( MAINBU NVARCHAR2(200)) ;
update QEWEB_VENDOR_BASE_INFO set MAINBU = MAINBU_1;
alter table QEWEB_VENDOR_BASE_INFO drop column MAINBU_1;