ALTER TABLE QEWEB_BASE_LOG DROP COLUMN LOG_CONTENT; 
commit;
ALTER TABLE QEWEB_BASE_LOG ADD (LOG_CONTENT CLOB NULL ); 