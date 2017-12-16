alter table QEWEB_RECEIVE_ITEM
add (VIEW_NO_CHECK_ITEM_ID NUMBER(10) default ‘’ not null);

alter table QEWEB_PURCHASE_ORDER_ITEM
add (VIEW_NO_CHECK_ITEM_ID NUMBER(10) default ‘’ not null);
