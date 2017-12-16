alter table qeweb_bill_list add (po_number NVARCHAR2(255) NULL  ) ;

alter table qeweb_bill_list_item add (line_number NUMBER(9) NULL  ) ;
alter table qeweb_bill_list_item add (sync_status NUMBER(2) NULL  ) ;