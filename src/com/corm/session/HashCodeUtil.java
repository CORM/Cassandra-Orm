/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class HashCodeUtil {

	static long computeHashOverload(Object object,String name,List<Object> objects){
		final com.corm.mapping.generated.Class clazz= ORMResourceRegistry.getClass(name);
		return ClassDescriptorRegistry.get(name).hash() + hash(ORMResourceRegistry.getColumnSet(clazz.getName()),object, objects);	
	}
	
	static int computeDeleteHash(Object object,String name){
		com.corm.mapping.generated.Class clazz= ORMResourceRegistry.getClass(name);
		ClassDescriptor cd = ClassDescriptorRegistry.get(name);
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(clazz.getName());
		Set<String> hashingList= new LinkedHashSet<String>();
		String methodName=null;
		if(null == object){
			for(com.corm.mapping.generated.Column property:setProperties){
				hashingList.add(cd.getMethod(property.getName()));
			}
		}else{
			for(com.corm.mapping.generated.Column property:setProperties){
				methodName = cd.getMethod(property.getField());
				if(isSet(cd,object,methodName)){
					hashingList.add(cd.getMethod(property.getName()));
				}
			}			 
		}
		return compute(hashingList);
	}
	
	private static int compute(Set<String> toCompute){
		StringBuilder builder = new StringBuilder();
		for(String value:toCompute){
			builder.append(value);
		}
		builder.trimToSize();
		int hash = builder.toString().hashCode();
		builder.setLength(0);
		builder = null;
		toCompute.clear();
		return hash;
	}

	private static boolean isSet(ClassDescriptor cd,Object object, String methodName){
		boolean valueSet=false;
		try {
			Method method = cd.method(methodName);
			Object obj = method.invoke(object) ;
			if(null != obj){
				valueSet=true;
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException  | SecurityException e) {
		}
		return valueSet;
	}

	private static long hash(Set<com.corm.mapping.generated.Column> setProperties,Object object,List<Object> objects){
		long hash=0;
		try {
			for(com.corm.mapping.generated.Column property:setProperties){
				Object obj = property.classField().get(object);
				if(null != obj){
					hash +=property.hash();
					objects.add(obj);
				}
			}
			
		} catch (IllegalAccessException | IllegalArgumentException
				|  SecurityException e) {
		}
		return hash;
	}	
}
