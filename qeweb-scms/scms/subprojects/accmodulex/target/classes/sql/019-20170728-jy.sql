alter table qeweb_delivery add purchasing_group_code nvarchar2(50);		--采购组编码
alter table qeweb_delivery add purchasing_group_id number(9);		--采购组Id
alter table qeweb_receive add purchasing_group_code nvarchar2(50);		--采购组编码
alter table qeweb_receive add purchasing_group_id number(9);		--采购组Id