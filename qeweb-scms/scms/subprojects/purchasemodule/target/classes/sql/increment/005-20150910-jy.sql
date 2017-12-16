alter table qeweb_inventory add batch_num nvarchar2(100); 	/*批次号*/
alter table qeweb_inventory add refer_to nvarchar2(200);	/*参考*/
alter table qeweb_inventory add stock_status number(10);	/*状态(区分不同的库存:0表示同步接口库存,1表示VMI库存)*/