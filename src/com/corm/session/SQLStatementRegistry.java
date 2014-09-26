/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import java.util.Map;
import java.util.logging.Logger;

public class SQLStatementRegistry {
	private static final Logger LOGGER=Logger.getLogger(SQLStatementRegistry.class.getName());

	private static Map<Long,ORMSQLContext> mpCreateSQL=new HashMap<Long,ORMSQLContext>();
	private static Map<Long,ORMSQLContext> mpDeleteSQL=new HashMap<Long,ORMSQLContext>();
	
	private static Map<String,String> mpSelectSQL=new HashMap<String,String>();
	
	public static void register(com.corm.mapping.generated.Class clazz){
		mpSelectSQL.put(clazz.getName(),buildSelectColumnList(clazz));	
		ORMClassRegistry.register(clazz);
	}

	static ORMSQLContext getCreate(Object object,List<Object> objects){
		String clazzName=ORMResourceRegistry.getClassName(object);
		long hashCode=HashCodeUtil.computeHashOverload(object, clazzName,objects);
		ORMSQLContext context = mpCreateSQL.get(hashCode);
		if(null == context){	
			synchronized(mpCreateSQL){
					if(null == context){
						context = buildCreateColumnList(object,ORMResourceRegistry.getClass(clazzName),"INSERT INTO","create");
						LOGGER.info("Caching Create SQL(hashcode:"+hashCode+") "+context.getSQL());
						mpCreateSQL.put(hashCode, context);	
					}
			}
		}
		clazzName=null;
		return context;
	}
		
	static ORMSQLContext getDelete(String clazzName,Object object,List<Object> objects){
		long hashCode=HashCodeUtil.computeHashOverload(object, clazzName,objects);
		ORMSQLContext context = mpDeleteSQL.get(hashCode);
		if(null == context){	
			synchronized(mpDeleteSQL){
				if(null == context){
					context = buildDeleteColumnList(object,ORMResourceRegistry.getClass(clazzName));
					LOGGER.info("Caching Delete SQL:"+context.getSQL());
					mpDeleteSQL.put(hashCode, context);	
				}
			}
		}
		return context;
	}
	
	public static String getSelect(String clazzName){
		return mpSelectSQL.get(clazzName);
	}
	
	private static ORMSQLContext buildCreateColumnList(Object obj, com.corm.mapping.generated.Class clazz,String action,String ignoreFor){
		
//		String catalog = clazz.getKeyspace();
		String table = clazz.getColumnFamily();
		StringBuilder builder = new StringBuilder();
		builder.append(action+" ").append( table + " (" );
		Set<String> columns = ORMPropertyListBuilder.build(obj, clazz.getName(),ignoreFor);
		Set<com.corm.mapping.generated.Column> refined = new LinkedHashSet<com.corm.mapping.generated.Column>();

		for(String column: columns){
//			String name = column.getName();
			builder.append(column+',');			
		}

		builder.trimToSize();		
		builder = new StringBuilder(builder.substring(0,builder.length()-1));
		builder.append(") VALUES (");
		

		for(int i=0; i < columns.size();i++){
//			if(column.isInject() && !canIgnore(column,ignoreFor)){
//				builder.append(column.getHint()+",");
//			}else{
//				refined.add(column);
				builder.append("?,");
//			}
		}

		
		String sql=builder.substring(0,builder.length()-1) + ")";
		builder.setLength(0);
		builder = null;
		return new ORMSQLContext(sql,refined);
		
	}
	

	private static ORMSQLContext buildDeleteColumnList(Object obj, com.corm.mapping.generated.Class clazz){
		
//		String catalog = clazz.getKeyspace();
		String table = clazz.getColumnFamily();
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM " + table+" where " );

		Set<com.corm.mapping.generated.Column> properties = ORMPropertyListBuilder.buildDelete(obj, clazz.getName());

		for(com.corm.mapping.generated.Column column: properties){
			String name = column.getName();
			builder.append(name).append("=? AND ");			
		}
		

		builder.trimToSize();		

		int backTrack=(properties.size() == 0)?4:0;
		
		
		String sql=builder.substring(0,builder.length() -backTrack) ;
		
		return new ORMSQLContext(sql,properties);
		
	}	
	private static String buildSelectColumnList(com.corm.mapping.generated.Class clazz){
//		String catalog = clazz.getKeyspace();
		String table = clazz.getColumnFamily();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");

		for(com.corm.mapping.generated.Column Column: clazz.getColumn()){

			String column = Column.getName();
			builder.append(column+',');			
		}
		builder.trimToSize();		
		String ret =   builder.substring(0,builder.length()-1) + " FROM "+ table+" ";
		builder.setLength(0);
		builder = null;
		return ret;

	}
}
