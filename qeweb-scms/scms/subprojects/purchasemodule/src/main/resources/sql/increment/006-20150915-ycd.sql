ALTER TABLE QEWEB_ACCOUNT_PAYABLE_LEDGER ADD (MONTH NVARCHAR2(6));
COMMENT ON COLUMN QEWEB_ACCOUNT_PAYABLE_LEDGER.MONTH IS '台账月';