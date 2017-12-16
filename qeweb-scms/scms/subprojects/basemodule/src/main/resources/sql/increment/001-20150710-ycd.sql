ALTER TABLE QEWEB_MATERIAL ADD (vesion NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.vesion IS '版本';

ALTER TABLE QEWEB_MATERIAL ADD (edition NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.edition IS '版次';

ALTER TABLE QEWEB_MATERIAL ADD (reference_Num NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.reference_Num IS '参图号';

ALTER TABLE QEWEB_MATERIAL ADD (mappable_Unit NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.mappable_Unit IS '图幅';

ALTER TABLE QEWEB_MATERIAL ADD (weight NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.weight IS '重量';

ALTER TABLE QEWEB_MATERIAL ADD (grade NVARCHAR2(54));
COMMENT ON COLUMN QEWEB_MATERIAL.grade IS '分级';