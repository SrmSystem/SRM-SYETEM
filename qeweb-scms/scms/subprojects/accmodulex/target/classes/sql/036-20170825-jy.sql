--修改预测计划待确认的待办
update QEWEB_BACKLOG_CFG 
set QUERY_SQL = 'SELECT COUNT (DISTINCT A . ID)FROM qeweb_purchase_plan_item A INNER JOIN qeweb_purchase_plan b ON A .plan_id = b. ID WHERE A .publish_status = 1 AND A .abolished = 0 AND A .confirm_status = 0 AND b.publish_status <> 0 AND A .vendor_id = #currentOrgId#' 
where id = '6056656'


--修改预测计划反馈处理结果待确认的待办
update QEWEB_BACKLOG_CFG 
set QUERY_SQL = 'SELECT COUNT (DISTINCT A . ID)FROM qeweb_purchase_plan_item A INNER JOIN qeweb_purchase_plan b ON A .plan_id = b. ID WHERE A .publish_status = 1 AND A .abolished = 0 AND A .confirm_status = -2 AND b.publish_status <> 0 AND A .vendor_id = #currentOrgId#' 
where id = '6056658'