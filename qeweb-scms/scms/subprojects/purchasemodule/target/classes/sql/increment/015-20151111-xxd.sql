alter table qeweb_bill_list_item add (tax NUMBER(11,2));
alter table QEWEB_BILL_LIST drop (buyer_id);
alter table qeweb_bill_list add (buyer_id NUMBER(9));
