package com.qeweb.sap;

public class SapTableStructure {
	//sap接口
 public static final String RETURN_E_FLAG="E_FLAG";//返货标识 Y成功 N失败
 public static final String RETURN_E_MESS="E_MESS";//返回信息
	
  //1、公司	
  public static final String COMPANY_ZEIP_GET_COM_NAM="ZEIP_GET_COM_NAM"; //公司接口名
  public static final String COMPANY_TABLE_NAME="ET_T001"; //公司表名
  public static final String COMPANY_ZEIP_GET_COM_NAM_BUKRS="BUKRS";//公司编码
  public static final String COMPANY_ZEIP_GET_COM_NAM_BUTXT="BUTXT"; //公司名称
  
  //2、工厂
  public static final String FACTORY_ZEIP_POINFO_GETWERKS="ZEIP_POINFO_GETWERKS";//工厂接口名
  public static final String FACTORY_TABLE_NAME="IT_WERKS";//工厂表名
  public static final String FACTORY_ZEIP_POINFO_GETWERKS_WERKS="WERKS";//编码
  public static final String FACTORY_ZEIP_POINFO_GETWERKS_NAME1="NAME1";//名称
  
  //3、采购组织
  public static final String ORGANIZATION_ZEIP_POINFO_GETEKORG="ZEIP_POINFO_GETEKORG";//采购组织接口名
  public static final String ORGANIZATION_TABLE_NAME="IT_EKORG";//采购组织表名
  public static final String ORGANIZATION_ZEIP_POINFO_GETEKORG_EKORG="EKORG";//编码
  public static final String ORGANIZATION_ZEIP_POINFO_GETEKORG_EKOTX="EKOTX";//名称
  
  
  //4、供应商信息
  public static final String VENDOR_Z_EIP_MM_GETLIFTXT="Z_EIP_MM_GETLIFTXT";//接口名
  public static final String VENDOR_TABLE_NAME="Z_EIP_MM_GETLIFTXT";//表名
  public static final String VENDOR_Z_EIP_MM_GETLIFTXT_I_LIFNR="I_LIFNR";//供应商代码 输入
  public static final String VENDOR_Z_EIP_MM_GETLIFTXT_E_NAME1="E_NAME1";//供应商名称 输出
  
  
  //5、公司对应工厂信息
  public static final String COMPANY_FACTORY_Z_BI_BUKRS_WERKS="Z_BI_BUKRS_WERKS";//接口名
  public static final String COMPANY_FACTORY_TABLE_NAME="Z_BI_BUKRS_WERKS";//表名
  public static final String COMPANY_FACTORY_Z_BI_BUKRS_WERKS_BUKRS="BUKRS";//公司代码
  public static final String COMPANY_FACTORY_Z_BI_BUKRS_WERKS_BUTXT="BUTXT";//公司名称
  public static final String COMPANY_FACTORY_Z_BI_BUKRS_WERKS_WERKS="WERKS";//工厂代码
  public static final String COMPANY_FACTORY_Z_BI_BUKRS_WERKS_NAME1="NAME1";//工厂名称
  
  
  //6、采购组
  public static final String PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP="ZEIP_POINFO_GETEKGRP";//采购组接口名
  public static final String PURCHASING_GROUP_TABLE_NAME="IT_EKGRP";
  public static final String PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP_EKGRP="EKGRP";//采购组编码
  public static final String PURCHASING_GROUP_ZEIP_POINFO_GETEKGRP_EKNAM="EKNAM";//采购组名称
  
  //7、采购类型
  public static final String PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE="Z_SRM_MM_POTYPE";//采购类型接口名
  public static final String PURCHASE_ORDER_TABLE_NAME="ITAB";//表名
  public static final String PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE_BSART="BSART";//编码
  public static final String PURCHASE_ORDER_TYPE_Z_SRM_MM_POTYPE_BATXT="BATXT";//名称
  
  //8、工厂对应采购组织
  public static final String GROUP_FACTORY_ZEIP_POINFO_GETWEREKO="ZEIP_POINFO_GETWEREKO";//接口名
  public static final String GROUP_FACTORY_TABLE_NAME="IT_WEREKO";//表名
  public static final String GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_WERKS="WERKS";//工厂编码
  public static final String GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_NAME1="NAME1";//工厂名称
  public static final String GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_EKORG="EKORG";//采购组织编码
  public static final String GROUP_FACTORY_ZEIP_POINFO_GETWEREKO_EKOTX="EKOTX";//采购组织名称
  
  //9、要货计划
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT="Z_SRM_GET_DELIVERY_SPLIT";//接口名
  //import
  public static final String PURCHASE_REQUEST_IMPORT_I_SEL="I_SEL";//1、查询材料 2、查询成品
  public static final String PURCHASE_REQUEST_IMPORT_I_WERKS="I_WERKS";//工厂
  //table_name
  public static final String PURCHASE_REQUEST_TABLE_NAME="IT_MATNR";//表名
  //export
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_WERKS="WERKS";//工厂
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MATNR="MATNR";//物料号
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MAKTX="MAKTX";//物料描述
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_EKGRP="EKGRP";//采购组
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_LIFNR="LIFNR";//供应商
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_RQ="RQ";//日期
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_ZB="ZB";//占比
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_JHZZT="JHZZT";//计划周转天数
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_ZLJYT="ZLJYT";//质量检验天数
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_SHPL="SHPL";//送货频率（天数）
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_YSTS="YSTS";//运输天数
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_PCSL="PCSL";//排产数量
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_DHSL="DHSL";//到货数量
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_KCSL="KCSL";//库存数量
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_FHSL="FHSL";//发货数量
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_FLAG="FLAG";//新品/量产标识（X-新品，空白 - 量产）
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_MEINS="MEINS";//单位
  public static final String PURCHASE_REQUEST_Z_SRM_GET_DELIVERY_SPLIT_BISMT="BISMT";//旧物料号
  
  
  //10.删除DN号
  public static final String DN_DEL_Z_SRM_MM_DNDELETE="Z_SRM_MM_DNDELETE";//接口名
  public static final String DN_DEL_TABLE_NAME="IT_RETURN";//表名？？？TODO
  public static final String DN_DEL_Z_SRM_MM_DNDELETE_VBELN="VBELN";//内向单号
  public static final String DN_DEL_Z_SRM_MM_DNDELETE_FLAG="FLAG";//返回标识
  public static final String DN_DEL_Z_SRM_MM_DNDELETE_MESS="MESS";//返回信息
  
  
  //11、创建DN号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE="Z_SRM_MM_DNCREATE";//接口名
  public static final String DN_CREATE_TABLE_NAME="??未给出TODO";//表名
  //输入
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_LIFNR="LIFNR";//供应商账号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_EBELN="EBELN";//采购订单号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_EBELP="EBELP";//行号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_LFIMG="LFIMG";//实际已交货量（按销售单位）
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_MEINS="MEINS";//基本单位
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_CHARG="CHARG";//批号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_HSDAT="HSDAT";//生产日期
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_VERSION="VERSION";//文本行
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_BOLNR="BOLNR";//发货单号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_ANZPK="ANZPK";//交货中总的包数
  
  //输出
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_VBELN="VBELN";//DN号
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_POSNR="POSNR";//交货项目
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_FLAG="FLAG";//标识
  public static final String DN_CREATE_Z_SRM_MM_DNCREATE_MESS="MESS";//返回信息
  
  
  //12、修改DN号
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE="Z_SRM_MM_DNCHANGE";//接口名称
  public static final String DN_UPDATE_TABLE_NAME="IT_RETURN";//表名 
  //输入
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_VBELN="VBELN";//DN号
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_POSNR="POSNR";//交货项目
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_LFIMG="LFIMG";//发货数量
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_MEINS="MEINS";//基本计量单位
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_CHARG="CHARG";//批号
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_HSDAT="HSDAT";//生产日期
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_ANZPK="ANZPK";//交货中总的包数
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_VERSION="VERSION";//文本行
  
  //输出
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_FLAG="FLAG";//标识
  public static final String DN_UPDATE_Z_SRM_MM_DNCHANGE_MESS="MESS";//返回信息
  
  //采购计算开始
  //采购结算信息
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENT="Z_SRM_MM_PURCHASE_STATEMENT";//采购计算接口名称
  //输出
  public static final String ORDER_SETTLEMEN_ET_STATE="ET_STATE";//输出的表名
  public static final String ORDER_SETTLEMEN_IT_BUDAT="IT_BUDAT";//输入的表名
  //export
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EBELN="EBELN";//采购凭证号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EBELP="EBELP";//采购凭证的项目编号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WERKS="WERKS";//工厂
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WERKSMS="WERKSMS";//工厂名称
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKORG="EKORG";//采购组织
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKORGMS="EKORGMS";//采购组织描述
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_LIFNR="LIFNR";//供应商或债权人的帐号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_LIFNRMS="LIFNRMS";//供应商名称 
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKGRP="EKGRP";//采购组 
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_EKGRPMS="EKGRPMS";//采购组的描述
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_MATNR="MATNR";//物料号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_TXZ01="TXZ01";//短文本
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_MEINS="MEINS";//基本计量单位
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_POQTY="POQTY";//订单数量
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BUDAT="BUDAT";//凭证中的过帐日期
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BELNR="BELNR";//物料凭证编号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BUZEI="BUZEI";//物料凭证中的项目
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_GRQTY="GRQTY";//入库数量
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_IVQTY="IVQTY";//开票数量
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_OPQTY="OPQTY";//仍要开票数量(grqty - ivqty)
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_VBELN="VBELN";//DN号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_BOLNR="BOLNR";//ASN单号
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_WAERS="WAERS";//货币码 
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_PRICE="PRICE";//不含税单价
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_VALUE="VALUE";//仍要开票的净价值
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_AEDAT="AEDAT";//更改日期
  public static final String ORDER_SETTLEMEN_Z_SRM_MM_PURCHASE_STATEMENTELN_RETPO="RETPO";//退货项目 

  //采购计划开始
  public static final String PURCHASE_PLANNING_BOARD_Z_SRM_DELIVERY_SPLIT="Z_SRM_DELIVERY_SPLIT";//采购计算接口名称
  //输出
  public static final String PURCHASE_PLANNING_BOARD_IT_VEN_FORMATED="IT_VEN_FORMATED";//采购员看板输出的表名
  public static final String PURCHASE_PLANNING_BOARD_IT_MAT_FORMATED="IT_MAT_FORMATED";//采购计划看板输出的表名
  
  
}
