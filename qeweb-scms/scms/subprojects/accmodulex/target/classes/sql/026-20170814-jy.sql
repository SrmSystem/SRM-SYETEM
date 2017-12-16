--修改待办表中字段的精度
ALTER TABLE QEWEB_BACKLOG_CFG modify QUERY_HQL NVARCHAR2(1000);
ALTER TABLE QEWEB_BACKLOG_CFG modify QUERY_SQL NVARCHAR2(1000);

--修改采购商待办的SQL,增加采购组
update QEWEB_BACKLOG_CFG set QUERY_SQL='select count(*) from PURCHASE_GOODS_REQUEST a where a.id in(select GOODS_REQUEST_ID from QEWEB_PURCHASE_ORDER_ITEM_PLAN orderPlan inner join QEWEB_PURCHASE_ORDER puOrder on ORDERPLAN.ORDER_ID = PUORDER.ID where orderPlan.PUBLISH_STATUS =0 and orderPlan.CONFIRM_STATUS = -1 and PUORDER.PURCHASING_GROUP_ID in #currentGroupId#)' where id = 6056545;
update QEWEB_BACKLOG_CFG set QUERY_HQL='select count(*) from PurchaseOrderEntity a where a.publishStatus=1 and a.confirmStatus = -1 and a.abolished=0 and a.purchasingGroup.id in #currentGroupId#' where id = 6056636;
update QEWEB_BACKLOG_CFG set QUERY_HQL='select count(*) from PurchasePlanEntity a where a.publishStatus=0 and a.abolished = 0 and a.group.id in #currentGroupId#' where id = 6056434;
update QEWEB_BACKLOG_CFG set QUERY_HQL='select count(*) from PurchaseGoodsRequestEntity a where a.publishStatus=0 and a.abolished=0 and a.group.id in #currentGroupId#' where id = 6056495;
update QEWEB_BACKLOG_CFG set QUERY_SQL='select count(*) from QEWEB_DELIVERY where AUDIT_STATUS=0 and ABOLISHED= 0 and purchasing_group_id in #currentGroupId#' where id = 6056563;
update QEWEB_BACKLOG_CFG set QUERY_SQL='select count(DISTINCT a.PLAN_ID) from qeweb_purchase_plan_item a  inner join QEWEB_PURCHASE_PLAN b on a.plan_id = b.id where a.publish_Status = 1 and a.confirm_Status = -1 and b.group_id in #currentGroupId#' where id = 6056654;
