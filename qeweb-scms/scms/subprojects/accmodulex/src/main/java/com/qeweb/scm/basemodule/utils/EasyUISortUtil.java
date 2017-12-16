package com.qeweb.scm.basemodule.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
/**
 * @author VILON
 *
 */
public class EasyUISortUtil{
	/**
	 * easyui 排序时使用，支持单列排序和多列排序
	 * @param sort easyui中sortable=true时，传到后台的参数，多个以逗号分隔
	 * @param order easyui中sortable=true时，传到后台的参数，多个以逗号分隔
	 * @return 返回Sort对象
	 */
	public static Sort getSort(String sort,String order){
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		if(sort!=null&&order!=null){
			String []ps = sort.split(",");
			String []os = order.split(",");
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
		Sort so = null;
		if(list.size()!=0){
			so = new Sort(list);
		}
		return so;
	}

}
