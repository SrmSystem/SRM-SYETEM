-- 订单明细价格
alter table qeweb_purchase_order_item add(price number(11,2));
alter table qeweb_goods_request_item add(price number(11,2));