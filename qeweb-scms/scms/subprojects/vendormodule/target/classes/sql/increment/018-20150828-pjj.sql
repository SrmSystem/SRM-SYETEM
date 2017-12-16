-- Add/modify columns 
alter table QEWEB_VENDOR_BASE_INFO modify mainbu NUMBER(9);
alter table QEWEB_VENDOR_BASE_INFO add qs_report NVARCHAR2(255);
-- Add comments to the table 
comment on table QEWEB_VENDOR_BASE_INFO
  is '供应商信息表';
-- Add comments to the columns 
comment on column QEWEB_VENDOR_BASE_INFO.id
  is '主键';
comment on column QEWEB_VENDOR_BASE_INFO.code
  is '编码';
comment on column QEWEB_VENDOR_BASE_INFO.name
  is '名称';
comment on column QEWEB_VENDOR_BASE_INFO.short_name
  is '简称';
comment on column QEWEB_VENDOR_BASE_INFO.property
  is '性质';
comment on column QEWEB_VENDOR_BASE_INFO.country
  is '国家';
comment on column QEWEB_VENDOR_BASE_INFO.province
  is '省份';
comment on column QEWEB_VENDOR_BASE_INFO.city
  is '城市';
comment on column QEWEB_VENDOR_BASE_INFO.address
  is '地址';
comment on column QEWEB_VENDOR_BASE_INFO.material_id
  is '主要产品ID';
comment on column QEWEB_VENDOR_BASE_INFO.material_type_id
  is '主要产品品类ID';
comment on column QEWEB_VENDOR_BASE_INFO.product_line
  is '主机厂';
comment on column QEWEB_VENDOR_BASE_INFO.last_year_income
  is '上年度销售收入（万元）';
comment on column QEWEB_VENDOR_BASE_INFO.employee_amount
  is '员工人数';
comment on column QEWEB_VENDOR_BASE_INFO.remark
  is '备注';
comment on column QEWEB_VENDOR_BASE_INFO.abolished
  is '废除标记';
comment on column QEWEB_VENDOR_BASE_INFO.create_time
  is '创建时间';
comment on column QEWEB_VENDOR_BASE_INFO.create_user_id
  is '创建用户FK';
comment on column QEWEB_VENDOR_BASE_INFO.last_update_time
  is '最后更新时间';
comment on column QEWEB_VENDOR_BASE_INFO.update_user_id
  is '最后更新用户FK';
comment on column QEWEB_VENDOR_BASE_INFO.create_user_name
  is '创建用户姓名';
comment on column QEWEB_VENDOR_BASE_INFO.update_user_name
  is '更新用户姓名';
comment on column QEWEB_VENDOR_BASE_INFO.duns
  is '邓白氏编码';
comment on column QEWEB_VENDOR_BASE_INFO.legal_person
  is '企业法人';
comment on column QEWEB_VENDOR_BASE_INFO.regtime
  is '企业成立时间';
comment on column QEWEB_VENDOR_BASE_INFO.stock_share
  is '股比构成';
comment on column QEWEB_VENDOR_BASE_INFO.main_product
  is '主要产品';
comment on column QEWEB_VENDOR_BASE_INFO.org_id
  is '关联组织ID';
comment on column QEWEB_VENDOR_BASE_INFO.phase_id
  is '当前所在阶段';
comment on column QEWEB_VENDOR_BASE_INFO.template_id
  is '当前所使用的模版';
comment on column QEWEB_VENDOR_BASE_INFO.ipo
  is '是否上市';
comment on column QEWEB_VENDOR_BASE_INFO.web_addr
  is '网址';
comment on column QEWEB_VENDOR_BASE_INFO.tax_id
  is '税务登记号';
comment on column QEWEB_VENDOR_BASE_INFO.ownership
  is '企业所有权';
comment on column QEWEB_VENDOR_BASE_INFO.bank_name
  is '银行汇报';
comment on column QEWEB_VENDOR_BASE_INFO.mainbu
  is '主供事业部';
comment on column QEWEB_VENDOR_BASE_INFO.reg_capital
  is '注册资本';
comment on column QEWEB_VENDOR_BASE_INFO.total_capital
  is '总资本';
comment on column QEWEB_VENDOR_BASE_INFO.working_capital
  is '流动总资金';
comment on column QEWEB_VENDOR_BASE_INFO.floor_area
  is '占地面积';
comment on column QEWEB_VENDOR_BASE_INFO.vendor_type
  is '供应商类型';
comment on column QEWEB_VENDOR_BASE_INFO.phase_cfg_id
  is '配置的阶段ID';
comment on column QEWEB_VENDOR_BASE_INFO.phase_sn
  is '配置的阶段顺序';
comment on column QEWEB_VENDOR_BASE_INFO.district
  is '区域';
comment on column QEWEB_VENDOR_BASE_INFO.country_text
  is '国家文本';
comment on column QEWEB_VENDOR_BASE_INFO.province_text
  is '省份文本';
comment on column QEWEB_VENDOR_BASE_INFO.city_text
  is '城市文本';
comment on column QEWEB_VENDOR_BASE_INFO.district_text
  is '区域文本';
comment on column QEWEB_VENDOR_BASE_INFO.product_power
  is '产能(万/台)';
comment on column QEWEB_VENDOR_BASE_INFO.versionno
  is '版本号';
comment on column QEWEB_VENDOR_BASE_INFO.current_version
  is '是否为当前版本';
comment on column QEWEB_VENDOR_BASE_INFO.audit_status
  is '审核状态';
comment on column QEWEB_VENDOR_BASE_INFO.approve_status
  is '审批状态';
comment on column QEWEB_VENDOR_BASE_INFO.submit_status
  is '提交状态';
comment on column QEWEB_VENDOR_BASE_INFO.audit_reason
  is '审核原因';
comment on column QEWEB_VENDOR_BASE_INFO.audit_user
  is '审核人';
comment on column QEWEB_VENDOR_BASE_INFO.vendor_level
  is '供应商等级';
comment on column QEWEB_VENDOR_BASE_INFO.vendor_classify
  is '供应商分类1';
comment on column QEWEB_VENDOR_BASE_INFO.vendor_classify2
  is '供应商分类2';
comment on column QEWEB_VENDOR_BASE_INFO.qsa
  is '质量体系审核';
comment on column QEWEB_VENDOR_BASE_INFO.qsa_result
  is '质量体系评优结果';
comment on column QEWEB_VENDOR_BASE_INFO.qs_report
  is '质量体系报告-文件路径';