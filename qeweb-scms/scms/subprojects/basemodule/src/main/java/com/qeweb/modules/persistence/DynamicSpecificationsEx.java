/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.qeweb.modules.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.QueryFilterCfgEntity;
import com.qeweb.scm.basemodule.service.QueryFilterCfgService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;

public class DynamicSpecificationsEx {  
	
	private static Log logger = LogFactory.getLog(DynamicSpecificationsEx.class);

	public static <T> Specification<T> bySearchFilterEx(final Collection<SearchFilterEx> filters, final Class<T> entityClazz) {
		return new Specification<T>() {
			@Override
			@SuppressWarnings("all")
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				List<Predicate> orPredicates = Lists.newArrayList();
				addUserDataRight(entityClazz, root, builder, predicates, orPredicates);
				//数据条件添加
				if (Collections3.isNotEmpty(filters)) {
					for (SearchFilterEx filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case NEQ:
							predicates.add(builder.notEqual(expression, filter.value));
							break;
						case ISNULL:
					    	 predicates.add(builder.isNull(expression));
					    	 break;
						case ISNOTNULL:
							predicates.add(builder.isNotNull(expression));
							break;
						case IN:
					        In in = builder.in(expression);
					        in.value(filter.value);
					        predicates.add(in);
							break;
					    case NIN:
					        In in1 = builder.in(expression);
					        in1.value(filter.value);
					        predicates.add(builder.not(in1));
							break;
						}
					}
				}
				
				builder.asc(root.get("id"));
				
				if (!predicates.isEmpty()) {
					if(!orPredicates.isEmpty()) {
						return builder.or(builder.and(predicates.toArray(new Predicate[predicates.size()])), 
								builder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
					} else {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
	
	/**
	 * 去除数据权限的
	 * @param filters
	 * @param entityClazz
	 * @return
	 */
	public static <T> Specification<T> bySearchFilterExNoUserData(final Collection<SearchFilterEx> filters, final Class<T> entityClazz) {
		return new Specification<T>() {
			@Override
			@SuppressWarnings("all")
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				List<Predicate> orPredicates = Lists.newArrayList();
				//数据条件添加
				if (Collections3.isNotEmpty(filters)) {
					for (SearchFilterEx filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case NEQ:
							predicates.add(builder.notEqual(expression, filter.value));
							break;
						case ISNULL:
					    	 predicates.add(builder.isNull(expression));
					    	 break;
						case ISNOTNULL:
							predicates.add(builder.isNotNull(expression));
							break;
						case IN:
					        In in = builder.in(expression);
					        in.value(filter.value);
					        predicates.add(in);
							break;
					    case NIN:
					        In in1 = builder.in(expression);
					        in1.value(filter.value);
					        predicates.add(builder.not(in1));
							break;
						}
					}
				}
				
				builder.asc(root.get("id"));
				
				if (!predicates.isEmpty()) {
					if(!orPredicates.isEmpty()) {
						return builder.or(builder.and(predicates.toArray(new Predicate[predicates.size()])), 
								builder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
					} else {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
	
	
	@SuppressWarnings("all")
	public static <T> Specification<T> bySearchFilterExSort(final Collection<SearchFilterEx> filters, final Class<T> entityClazz,final String sortName,final String sortType) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				List<Predicate> orPredicates = Lists.newArrayList();
				if (Collections3.isNotEmpty(filters)) {
					
					for (SearchFilterEx filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}
						
						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case NEQ:
							predicates.add(builder.notEqual(expression, filter.value));
							break;
						}
					}
				}
				addUserDataRight(entityClazz, root, builder, predicates, orPredicates);
				if("asc".equals(sortType)) {
					builder.asc(root.get(sortName));
				} else{
					builder.desc(root.get(sortName));
				}
				// 将所有条件用 and 联合起来
				if (!predicates.isEmpty()) {
					if(!orPredicates.isEmpty()) {
						return builder.or(builder.and(predicates.toArray(new Predicate[predicates.size()])), 
								builder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
					} else {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}
				
				return builder.conjunction();
			}
		};
	}
	
	@SuppressWarnings("all")
	private static <T> void addUserDataRight(final Class<T> entityClazz, Root<T> root, CriteriaBuilder builder, List<Predicate> predicates, List<Predicate> orPredicates) {
		try {
			QueryFilterCfgEntity cfg = QueryFilterCfgService.QUERY_FILTER_MAP.get(entityClazz.getSimpleName());
			if(cfg == null)    
				return;
			
			String[] dataNames = null;
			String[] dataTypes = null;
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			//只对采购商添加数据权限
			if(user.orgRoleType == null || user.orgRoleType.intValue() != OrgType.ROLE_TYPE_BUYER)
				return;
			
			
			Map<String, Set<Long>> dataRightMap = user.dataRight;
			dataNames = StringUtils.split(cfg.getDataNames(), ",");
			dataTypes = StringUtils.split(cfg.getDataTypes(), ",");
			List<Predicate> list = Lists.newArrayList();
			int limitSize = 1000;
			Set<Long> value = null;
			List<Long> valList = null;
			for(int i = 0; i < dataNames.length; i ++) {
				Path expression = root.get(dataNames[i]);
				In in = builder.in(expression);
				if("?".equals(dataTypes[i])) {
					value = dataRightMap.get("orgId") == null ? Sets.newHashSet(Long.MAX_VALUE) : dataRightMap.get("orgId");
				} else {
					value = dataRightMap.get(dataTypes[i]) == null ? Sets.newHashSet(Long.MAX_VALUE) : dataRightMap.get(dataTypes[i]);
		        }
				logger.error(dataTypes[i] + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dataRightMap.get(dataTypes[i]));
				valList = Lists.newArrayList(value);
				int size = valList.size()/limitSize + (valList.size()%limitSize > 0 ? 1 : 0);
				if(size <= 1) {
					in.value(value);
					list.add(in);
					continue;
				}
				
				List sublist = null;
				for(int j = 0 ; j < size ; j++){
					if(j == 0){
						sublist = valList.subList(0, limitSize > value.size() ? value.size(): limitSize);
						in.value(sublist);
						list.add(in);
					} else {
						sublist = valList.subList(j*limitSize, (j+1)*limitSize>valList.size()?valList.size():(j+1)*limitSize);
						Path expression2 = root.get(dataNames[i]);
						In in2 = builder.in(expression2);
						in2.value(sublist);
						Predicate predicate = builder.or(in2);
						orPredicates.add(predicate);
					}
				}
			}
			predicates.addAll(list);
		} catch (Exception e) {
			logger.error("current user is invalid..");
			throw new RuntimeException(e.getMessage());
		}
	}
}
