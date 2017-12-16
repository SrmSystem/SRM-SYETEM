package com.qeweb.scm.basemodule.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PageUtil {
	
	/**
	 * 创建spring分页请求
	 * @param pageNumber 页码
	 * @param pagzSize 每页记录数量
	 * @param sortType 排序字段
	 * @return 分页请求
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}else if(StringUtils.isNotEmpty(sortType)){
			sort = new Sort(Direction.ASC, sortType);
		}
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}

	public static PageRequest buildPageRequest(int pageNumber, int pageSize, String sortName, String sortType) {
		Sort sort = null;
		
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else{
			List<Sort.Order> list = new ArrayList<Sort.Order>();
			if(sortName!=null&&sortType!=null){
				String []ps = sortName.split(",");
				String []os = sortType.split(",");
				if(ps.length!=os.length){
					return null;
				}
				for(int i=0;i<ps.length;i++){
					Sort.Order o = null;
					if("desc".equalsIgnoreCase(os[i])){
						o = new Sort.Order(Direction.DESC, ps[i]);
					}else{
						o = new Sort.Order(Direction.ASC, ps[i]);
					}
					list.add(o);
				}
			}
			if(list.size()!=0){
				sort = new Sort(list);
			}
		}
		
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}
	
	public static PageRequest buildPageRequestList(int pageNumber, int pageSize, List<Order> orderList) {
		Sort sort = new Sort(orderList);
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}

}
