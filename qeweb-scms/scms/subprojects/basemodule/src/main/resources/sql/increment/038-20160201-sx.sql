ALTER TABLE "QEWEB_VENDOR_NOTICE_CFG"
ADD ( "ROLE_ID" NUMBER(9) NULL  ) 
ADD ( "NTYPE" NUMBER(2) NULL  ) ;

ALTER TABLE "QEWEB_VENDOR_NOTICE_CFG" RENAME COLUMN "VENDOR_ID" TO "ORG_ID";