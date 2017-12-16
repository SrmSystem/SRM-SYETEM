package com.qeweb.scm.contractmodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;

/**
 * 合同模板
 * @author u
 *
 */

@Entity
@Table(name = "qeweb_contract_module")
public class ContractModuleEntity extends EPBaseEntity{
	
	/**
	 * 合同模板名称
	 */
    private String name;
    
    /**
     * 合同模板编号
     */
    private String code;
    
    private Long contractType;	//模板类型：0=临时模板; 1=终极模板

    @Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="contract_type")
	public Long getContractType() {
		return contractType;
	}

	public void setContractType(Long contractType) {
		this.contractType = contractType;
	}
	
	
}
