--采购员看板
INSERT INTO "QEWEB_VIEW" ("ID", 
	"VIEW_CODE", 
	"VIEW_NAME", 
	"VIEW_ICON", 
	"VIEW_URL", 
	"VIEW_TYPE", 
	"PARENT_ID", 
	"IS_LEAF", 
	"MENU_SN", 
	"REMARK", 
	"ABOLISHED", 
	"CREATE_TIME", 
	"CREATE_USER_ID", 
	"LAST_UPDATE_TIME", 
	"UPDATE_USER_ID", 
	"CREATE_USER_NAME", 
	"UPDATE_USER_NAME", 
	"VIEW_NAME_ZH", 
	"PERMISSION", 
	"IS_VENDOR") 
VALUES (
	'60854691', 
	'buyerBoard', 
	'采购员看板', 
	NULL, 
	'manager/order/buyerBoard/', 
	'0', 
	'6052052', 
	'0', 
	'5',
	NULL, 
	'0', 
	TO_TIMESTAMP(' 2017-10-10 00:00:00:000000', 'SYYYY-MM-DD HH24:MI:SS:FF6'), 
	'1', TO_TIMESTAMP(' 2017-10-10 00:00:00:000000', 'SYYYY-MM-DD HH24:MI:SS:FF6'), 
	'1', 
	'超级管理员', 
	'超级管理员', 
	NULL, 
	NULL, 
	'N');
