ALTER TABLE QEWEB_BASE_LOG ADD (OPTUSER_NAME NVARCHAR2(100) NULL ); 
ALTER TABLE QEWEB_BASE_LOG ADD (PC_NAME NVARCHAR2(100) NULL ); 


--修改采购订单待确认的待办
update QEWEB_BACKLOG_CFG 
set QUERY_HQL = 'SELECT COUNT (*) FROM PurchaseOrderEntity A WHERE    A .publishStatus <> 0 AND A .confirmStatus = 0 AND A.frgke = ''R'' AND A .vendor.id = #currentOrgId#' 
where id = '6056485'


