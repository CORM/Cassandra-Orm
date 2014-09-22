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

import com.corm.mapping.generated.OrmMapping;

public class ClassDescriptorRegistry {
	private static Map<String,ClassDescriptor> mpClassDescriptor = new HashMap<String,ClassDescriptor>();
	static void register(OrmMapping resource){
		com.corm.mapping.generated.Class clazz = resource.getClazz(); 
		mpClassDescriptor.put(clazz.getName(), prepare(clazz));

		mpClassDescriptor.put(clazz.getName(), prepare(clazz));
	}	
	private static Class<?> getClass(String name){
		Class<?> clazz=null;
		try {
			clazz = Class.forName(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return clazz;
	} 
	private static ClassDescriptor prepare(com.corm.mapping.generated.Class clazz){
		List<com.corm.mapping.generated.Column> columns = clazz.getColumn();
		String catalog=clazz.getKeyspace(); 
		String table =clazz.getColumnFamily(); 
		Class <?> descriptor = getClass(clazz.getName());
		ClassDescriptor cd = new ClassDescriptor(catalog,table,descriptor.getName());
		for(com.corm.mapping.generated.Column column:columns){
			cd.add(descriptor,column.getField(),column.getType());
		}

		return cd;
	}
	
	public static ClassDescriptor get(String name){
		return mpClassDescriptor.get(name);
	}
}
