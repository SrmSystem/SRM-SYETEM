--修改QEWEB_PURCHASE_ORDER_ITEM_PLAN的DN类型由number改nvarchar(50)


ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM_PLAN
 ADD (DN2  NVARCHAR2(50));

update QEWEB_PURCHASE_ORDER_ITEM_PLAN set DN2=DN;

update QEWEB_PURCHASE_ORDER_ITEM_PLAN set DN2='0'|| DN2 where DN is not null;


alter table QEWEB_PURCHASE_ORDER_ITEM_PLAN drop (DN);


alter table QEWEB_PURCHASE_ORDER_ITEM_PLAN rename column DN2 to DN;