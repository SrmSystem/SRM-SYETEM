
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605518, 1, '报名中', 'hourglass.png', 'applicationStatus', 
    '报名状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605529, 2, '报名完成', 'tick.png', 'applicationStatus', 
    '报名状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605532, 0, '未开始', '01.png', 'applicationStatus', 
    '报名状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605540, 0, '未报价', '01.png', 'quoteStatus', 
    '报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605543, 2, '报价完成', 'tick.png', 'quoteStatus', 
    '报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7605546, 1, '报价中', 'hourglass.png', 'quoteStatus', 
    '报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609243, 2, '空', '20130410120031302_easyicon_net_16.png', 'Null', 
    '空置');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609251, 1, '审核通过', 'tick.png', 'audit', 
    '审核状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609288, 0, '未合作', '01.png', 'cooperationStatus', 
    '合作状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609291, 1, '已合作', 'tick.png', 'cooperationStatus', 
    '合作状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609299, 0, '未确认', '01.png', 'negotiatedCheckStatus', 
    '供方确认议价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609303, -1, '拒绝议价', 'delete.png', 'negotiatedCheckStatus', 
    '供方确认议价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609308, 1, '接受议价', 'tick.png', 'negotiatedCheckStatus', 
    '供方确认议价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609311, 1, '已议价', 'tick.png', 'negotiatedStatus', 
    '采方议价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609316, 0, '未议价', '01.png', 'negotiatedStatus', 
    '采方议价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609320, 0, '未重新报价', '01.png', 'requoteStatus', 
    '重新报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609323, 1, '重新报价', 'tick.png', 'requoteStatus', 
    '重新报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609326, 1, '已报价', 'tick.png', 'wholeQuoteStatus', 
    '报价状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7609337, 0, '未报价', '01.png', 'wholeQuoteStatus', 
    '报价状态');

Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7616292, 0, '未审核', 'hourglass.png', 'auditStatus', 
    '审核状态 ');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7616295, 1, '审核通过', 'accept.png', 'auditStatus', 
    '审核状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7616299, 2, '审核驳回', '01.png', 'auditStatus', 
    '审核状态');
Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (7616302, -1, '驳回', '01.png', 'auditStatus', 
    '审核状态');

Insert into QEWEB_STATUS_DICT
   (ID, STATUS_VALUE, STATUS_TEXT, STATUS_ICON, STATUS_TYPE, 
    STATUS_NAME)
 Values
   (8853813, 2, '提交审核', '01.png', 'audit', 
    '审核状态');
COMMIT;
