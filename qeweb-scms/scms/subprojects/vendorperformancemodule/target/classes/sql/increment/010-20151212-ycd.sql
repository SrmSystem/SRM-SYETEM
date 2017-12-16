
/**
 * 评估调分历史
 */
CREATE TABLE QEWEB_ASSESS_SCORES_TOTAL_HIS
(
  ID                NUMBER(9)       PRIMARY KEY      NOT NULL,
  SCORES_ID         NUMBER(9),
  ORG_ID            NUMBER(9),
  BRAND_ID          NUMBER(9),
  SCORE1            NUMBER(20,10),
  SCORE2            NUMBER(20,10),
  SCORE3            NUMBER(20,10),
  SCORE4            NUMBER(20,10),
  SCORE5            NUMBER(20,10),
  SCORE6            NUMBER(20,10),
  SCORE7            NUMBER(20,10),
  SCORE8            NUMBER(20,10),
  SCORE9            NUMBER(20,10),
  SCORE10           NUMBER(20,10),
  SCORE11           NUMBER(20,10),
  SCORE12           NUMBER(20,10),
  SCORE13           NUMBER(20,10),
  SCORE14           NUMBER(20,10),
  SCORE15           NUMBER(20,10),
  SCORE16           NUMBER(20,10),
  SCORE17           NUMBER(20,10),
  SCORE18           NUMBER(20,10),
  SCORE19           NUMBER(20,10),
  SCORE20           NUMBER(20,10),
  TOTALS1           NUMBER(20,10),
  TOTALS2           NUMBER(20,10),
  TOTALS3           NUMBER(20,10),
  TOTALS4           NUMBER(20,10),
  TOTALS5           NUMBER(20,10),
  ADJUST_REASON     NVARCHAR2(255),
  CREATE_USER_ID    NUMBER(11),
  CREATE_USER_NAME  NVARCHAR2(255),
  CREATE_TIME       DATE,
  UPDATE_USER_ID    NUMBER(11),
  UPDATE_USER_NAME  NVARCHAR2(255),
  LAST_UPDATE_TIME  DATE,
  ABOLISHED         NUMBER(1)                   NOT NULL
);