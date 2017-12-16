-- Add/modify columns 
alter table QEWEB_VENDOR_SURVEY_TEMPLATE add sn NUMBER(3);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_SURVEY_TEMPLATE.sn
  is '模版顺序';
  
-- Add/modify columns 
alter table QEWEB_VENDOR_SURVEY_CFG add survey_template_sn NUMBER(3);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_SURVEY_CFG.survey_template_sn
  is '调查表模版顺序';
--------------------------------------------------
--  Changed table qeweb_vendor_template_survey  --
--------------------------------------------------
-- Add/modify columns 
alter table QEWEB_VENDOR_TEMPLATE_SURVEY add survey_template_sn NUMBER(3);
-- Add comments to the columns 
comment on column QEWEB_VENDOR_TEMPLATE_SURVEY.survey_template_sn
  is '调查表模版顺序';