CREATE TABLE QEWEB_TIMETASK_SETTING (
	ID NUMBER (9) PRIMARY KEY NOT NULL,
	DAY NVARCHAR2 (100),
	MONTH NVARCHAR2 (100),
	task_name NVARCHAR2 (255),
	vendor_id NUMBER (9),
	RUN_TIME NVARCHAR2 (100),
	ABOLISHED NUMBER (1),
	CREATE_TIME TIMESTAMP (6),
	CREATE_USER_ID NUMBER (9),
	LAST_UPDATE_TIME TIMESTAMP (6),
	UPDATE_USER_ID NUMBER (9),
	CREATE_USER_NAME NVARCHAR2 (100),
	UPDATE_USER_NAME NVARCHAR2 (100)
);