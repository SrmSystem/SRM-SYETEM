--修改预测计划供应商驳回待确认的待办
update QEWEB_BACKLOG_CFG 
set QUERY_SQL = 'select count(DISTINCT a.PLAN_ID) from qeweb_purchase_plan_item a  inner join QEWEB_PURCHASE_PLAN b on a.plan_id = b.id where a.publish_Status = 1 and a.confirm_Status = -1 and a.group_id in #currentGroupId#' 
where id = '6056654'