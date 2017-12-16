alter table qeweb_inventory add eoq number(11,2);
alter table qeweb_inventory add unit_name nvarchar2(100);
alter table qeweb_inventory add rop number(11,2);
alter table qeweb_inventory add passage_qty number(11,2);
alter table qeweb_inventory add stock_max_qty number(11,2);
alter table qeweb_inventory add purchase_people nvarchar2(100); 
alter table qeweb_inventory add pur_advance_time date;
