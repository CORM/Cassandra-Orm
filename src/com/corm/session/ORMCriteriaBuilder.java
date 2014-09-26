/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.corm.db.ORMCriteria;
import com.corm.db.ORMCriterion;
import com.corm.db.ORMOrder;
import com.corm.db.ORMRestriction;
import com.corm.wrappers.ORMClass;

public class ORMCriteriaBuilder {
	private static final Logger LOGGER=Logger.getLogger(ORMCriteriaBuilder.class.getName());
	private static Map<ORMClass,String> mpSelectSQL = new HashMap<ORMClass,String>();
	
	public static String build(Object object,ORMCriteria criteria){
		ORMClass ormClass = ORMClassRegistry.get(object.getClass().getName());
		if(null == criteria){
			criteria = new ORMCriteria(object.getClass());
		}

		String sql = mpSelectSQL.get(ormClass);
		
		if(null == sql){
			sql =buildSelection(criteria, ormClass);
			mpSelectSQL.put(ormClass, sql); 
			LOGGER.info("Acquire Criteria Baseline and caching:"+sql);
		}		
		String name = ormClass.name();
		
		int index = name.lastIndexOf(".");
		if(index >0){
			name = name.substring(index+1);
		}
		
		criteria=enrich(object,criteria,ormClass);

		StringBuilder builder = new StringBuilder(sql);
		
		builder.append(buildWhereClause(criteria.criterions()));
		
		builder.append(buildOrder(criteria.order()));
		builder.append(criteria.limit());
		LOGGER.info("Criterion constructed SQL:"+builder.toString());
		return builder.toString();		
	}
	
	private static ORMCriteria enrich(Object object,ORMCriteria criteria,ORMClass ormClass){

		Set<com.corm.mapping.generated.Column> columns = ORMPropertyListBuilder.build(object);
		String className = object.getClass().getName();

		for(com.corm.mapping.generated.Column column: columns){
//			String rawType = column.getRawType();
			ValueObject obj = ORMResourceRegistry.getValue(className,object,column.getField(),column.getType());
			if(obj.getType() != SQLTypes.NULL){
				criteria.add(ORMRestriction.eq(column.getName(), obj));
			}
		}

		return criteria;			
	}

	private static String buildWhereClause(List<ORMCriterion> criterions){

		StringBuilder builder = new StringBuilder();
		if(criterions.size() >0){
			builder.append("WHERE ");
		}
		for (ORMCriterion criterion:criterions){
			builder.append(criterion.get()+ "AND ");
		}
		if(criterions.size() !=0){
			builder= new StringBuilder(builder.substring(0, builder.length()-4));
		}
		builder.trimToSize();
		return builder.toString();
	}
	private static String buildOrder(List<ORMOrder> orders){
		if(orders.size() == 0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		StringBuilder builderAsc = new StringBuilder();
		StringBuilder builderDesc = new StringBuilder();
		for (ORMOrder order:orders){
			if(order.isasc()){
				builderAsc.append(order.raw()+",");
			}else{
				builderDesc.append(order.raw()+",");
			}
		}
		builderAsc.trimToSize();
		builderDesc.trimToSize();
		if(builderAsc.length() >1){
			builder.append( builderAsc.substring(0, builderAsc.length()-1) + " ASC");
		}
		if(builderDesc.length() >1){
			if(builder.length() !=0){
				builder.append(",");
			}
			builder.append(builderDesc.substring(0, builderDesc.length()-1) + " DESC" );
		}
		builder.trimToSize();
		return ORMOrder.getOrderClause() + builder.toString();		
	}

	private static String buildSelection(ORMCriteria criteria,ORMClass ormClass){
		boolean hasDistinct = criteria.hasDistinct();
		StringBuilder builder = new StringBuilder("SELECT ");
		if(hasDistinct){
			builder.append("DISTINCT ");
		}
		String entity = criteria.entity();

		com.corm.mapping.generated.Class clazz= ORMResourceRegistry.getClass(entity);
//		String catalog = clazz.getKeyspace();
		String table = clazz.getColumnFamily();
		String name = clazz.getName();
		
		int index = name.lastIndexOf(".");
		if(index >0){
			name = name.substring(index+1);
		}

// prepare the main selection list		
		for(com.corm.mapping.generated.Column column:clazz.getColumn()){
			builder.append(column.getName()+",");
		}
		builder.trimToSize();
		String sql=builder.substring(0,builder.length()-1) ;
		
		sql += " FROM " +table;// + name + " " ;
		builder = new StringBuilder();

		sql +=  builder.toString().trim()  + " "; 
		
		return sql;		
	}
	
}
