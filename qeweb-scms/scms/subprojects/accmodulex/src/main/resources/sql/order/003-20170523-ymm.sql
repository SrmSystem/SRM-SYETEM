ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (BSART  NVARCHAR2(100));
 
 ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (ZTERM  NVARCHAR2(100));
 
 ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (ZTERMMS  NVARCHAR2(100));
 
 ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (WAERS  NVARCHAR2(100));
 
 ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (WAERSMS  NVARCHAR2(100));
 
 ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (COMPANY_ID  NUMBER(11));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (PURCHASING_GROUP_ID NUMBER(11));
 
   ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (AEDAT  TIMESTAMP(6));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (ERNAM  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (FRGKE  NVARCHAR2(100));
 
   ALTER TABLE QEWEB_PURCHASE_ORDER
 ADD (ORDER_TYPE_ID  NUMBER(11));
 
 
 
 
 
 ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (KNTTP  NVARCHAR2(100));

  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (PSTYP  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (MENGE  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (MEINS  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (MATKL  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (MATKLMS  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (IDNLF  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (RETPO  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (ZFREE  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (LOEKZ  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (ZLOCK  NVARCHAR2(100));
 
  ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (KZABS  NVARCHAR2(100));
 
   ALTER TABLE QEWEB_PURCHASE_ORDER_ITEM
 ADD (FACTORY_ID  NUMBER(11));
