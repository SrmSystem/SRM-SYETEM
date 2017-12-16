package com.qeweb.scm.basemodule.repository.general;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

@Component
public class GenerialDao {
	@PersistenceContext
	private EntityManager em; 
	
	
	public Session getSession(){
		return em.unwrap(org.hibernate.Session.class);
	}
	/**
	 * 根据sql查询
	 * @param sql
	 * @return 返回类型为List<Object[]>
	 */	
	public List<?> queryBySql(String sql){
		Query query = em.createNativeQuery(sql);
		return query.getResultList();
	}
	
	public List<?> queryBySql(String sql, Map<String, Object> param){
		Query query = em.createNativeQuery(sql);
		if(param != null && param.size() > 0) {
			for(Map.Entry<String, Object> par : param.entrySet()) {
				query.setParameter(par.getKey(), par.getValue());
			}
		}
		return query.getResultList();
	}
	
	public List<?> queryByHql(String hql){
		Query query = em.createQuery(hql);
		return query.getResultList();
	}
	
	public <T> List<T> findAllByHql(Class<? extends T> entityClass, String hql) {  
		Query query =  em.createQuery(hql,entityClass);  
		List<T> EntityList = query.getResultList();  
		return EntityList;
	}
	
	/**
	 * 根据sql查询
	 * @param sql
	 * @return 返回类型为List<Map>
	 */
	public List<?> queryBySql_map(String sql){
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}

	/**
	 * 根据完整的hql查询数量
	 * @param hql hql
	 * @return
	 */
	public Integer findCountByHql(String hql) {
		Query query = em.createQuery(hql);
		long count = (Long) query.getSingleResult();
		return (int) count;
	}

	/**
	 * 根据完整的sql查询数量
	 * @param hql hql
	 * @return
	 */
	public Integer findCountBySql(String sql) {
		Query query = em.createNativeQuery(sql);
		return ((BigDecimal)query.getSingleResult()).intValue();
	}
	
	/**
	 * 分页。
	 * @param sql
	 * @param page
	 * @return 返回List<Map>类型的数据。
	 */
	public List<?> querybysql(String sql,Pageable page){
		Sort sort = page.getSort();
		String orderBy = "";
		boolean isFirst = true;
		int pageNum = page.getPageNumber();
		int pageSize = page.getPageSize();
		if(null != sort ){
			if(sort.iterator().hasNext()){
				Order o = sort.iterator().next();
				Direction d = o.getDirection();
				String prop = o.getProperty();
				if(isFirst){
					orderBy += " order by " + prop + " " + d.name();
					isFirst = false;
				}else{
					orderBy += "," + prop + " " + d.name();
				}
			}
		}

		Query query = em.createNativeQuery(sql + orderBy);
		query.setFirstResult(pageNum*pageSize);
		query.setMaxResults(pageSize);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}
	
	public List<?> querybyhql(String sql,int pageNum,int pageSize){
		Query query = em.createQuery(sql);
		query.setFirstResult((pageNum-1)*pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}
	
	/**
	 * 分页。
	 * @param sql
	 * @param page
	 * @return 返回List<Map>类型的数据。
	 */
	public int query(String sql){
		Query query = em.createNativeQuery(sql);
		return query.executeUpdate();
	}

	/**
	 * hql分页
	 * @param hql
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<Object[]> queryByHql(String hql, int pageNumber, int pageSize) {
		Query query = em.createQuery(hql);
		query.setFirstResult((pageNumber-1)*pageSize); 
		query.setMaxResults(pageSize); 
		return query.getResultList();
	}
	
}
