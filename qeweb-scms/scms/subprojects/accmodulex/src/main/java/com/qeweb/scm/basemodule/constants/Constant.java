package com.qeweb.scm.basemodule.constants;

/**
 * 
 * @author pjjxiajun
 * @date 2015年8月18日
 * @path com.qeweb.scm.basemodule.constants.Constant.java
 */
public interface Constant {
	/**
	 * 编码UTF-8
	 */
	public static final String ENCODING_UTF8 = "UTF-8";
	
	/**
	 * 删除状态
	 */
	public static final Integer DELETE_FLAG = 1;		//已删除
	public static final Integer UNDELETE_FLAG = 0;		//未删除
	/**供应类别1：代理供应商*/
	public static final Integer VENDOR_TYPE_1 = 1;
	/**供应类别2：生产供应商*/
	public static final Integer VENDOR_TYPE_2 = 2;		
	/**供应类别3：服务供应商*/
	public static final Integer VENDOR_TYPE_3 = 3;		
	/**供应类别4：经销供应商*/
	public static final Integer VENDOR_TYPE_4 = 4;	
	/**主供事业部0:主供事业部*/
	public static final Integer MAIN_BU_0 = 0;
	/**供应商阶段register：注册供应商*/
	public static final String VENDOR_PHASE_REGISTER = "register";		
	/**供应商阶段trial：体系外备选供应商*/
	public static final String VENDOR_PHASE_TRIAL = "trial";		
	/**供应商阶段official：体系内供应商*/
	public static final String VENDOR_PHASE_OFFICIAL = "official";		
	/**供应商阶段1：注册供应商*/
	public static final Integer VENDOR_PHASESN_1 = 1;		
	/**供应商阶段2：体系外备选供应商*/
	public static final Integer VENDOR_PHASESN_2 = 2;		
	/**供应商阶段3：体系内供应商*/
	public static final Integer VENDOR_PHASESN_3 = 3;		
	/**
	 * 供应商分类
	 */
    public static final String[] VEND_CLASSIFY = new String[]{"战略","核心","一般","淘汰","备选"};	
    
    /**
     * 供应商分类（ABC）
     */
    public static final String[] VEND_CLASSIFY2 = new String[]{"A","B","C"};	
    /**
     * 供应等级
     */
    public static final String[] VEND_LEVEL = new String[]{"国际水平","国内领先","国内一般"};
    
    /**
     * 图纸状态
     */
    public static final String[] PIC_STATUS = new String[]{"A","S","B"};
    /**
     * 绩效参数名称
     */
    public static final String[] VENDORPERFORPARAMETER_PARAMETER = new String[]{"装车量","市场PPM目标值","生产PPM目标值"};
    
}
