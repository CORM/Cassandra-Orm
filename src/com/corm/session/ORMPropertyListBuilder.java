/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ORMPropertyListBuilder {
	
	static Set<String> build(Object object,String name,String ignoreFor){
		Set<String> set = new LinkedHashSet<String>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){
			if((isSet(object,property.classField()) || property.isInject() && !canIgnore(property,ignoreFor)  ) ){
				set.add(property.getName());
			}
		}		
		return set;
	}
	static Set<com.corm.mapping.generated.Column> build(Object object){
		String name = object.getClass().getName();
		Set<com.corm.mapping.generated.Column> set = new LinkedHashSet<com.corm.mapping.generated.Column>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){
//			String methodName = cd.getMethod(property.getField());
			if(isSet(object,property.classField())){
				set.add(property);
			}
		}		
		return set;
	}
	static Set<com.corm.mapping.generated.Column> buildDelete(Object object,String name){
		ClassDescriptor cd = ClassDescriptorRegistry.get(name);
		Set<com.corm.mapping.generated.Column> set = new LinkedHashSet<com.corm.mapping.generated.Column>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){
			String methodName = cd.getMethod(property.getField());
			if((isSet(object,methodName))){
				set.add(property);
			}
		}		
		return set;
	}
	static Set<com.corm.mapping.generated.Column> buildDelete(Object object,String name,List<Object> objects){
		Set<com.corm.mapping.generated.Column> set = new LinkedHashSet<com.corm.mapping.generated.Column>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){

			if(isSet(property.classField(),object,objects)){
				set.add(property);
			}
		}		
		return set;
	}
	
	static Set<com.corm.mapping.generated.Column> build(String name){
		Set<com.corm.mapping.generated.Column> set = new LinkedHashSet<com.corm.mapping.generated.Column>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){
			set.add(property);
		}		
		return set;
	}	
	
	static Set<com.corm.mapping.generated.Column> getInjectableProperties(String name){
		Set<com.corm.mapping.generated.Column> set = new LinkedHashSet<com.corm.mapping.generated.Column>();
		Set<com.corm.mapping.generated.Column> setProperties = ORMResourceRegistry.getColumnSet(name);
		for(com.corm.mapping.generated.Column property:setProperties){
			if(property.isInject()){
				set.add(property);
			}
		}		
		return set;		
	}
	private static boolean canIgnore(com.corm.mapping.generated.Column property,String ignoreFor){
		String ignore=property.getIgnoreFor();
		if((null != ignore && (null != ignoreFor)) && ignore.indexOf(ignoreFor) >= 0){
			return true;
		}
		return false;
	}
	private static boolean isSet(Object object, String methodName){
		boolean valueSet=false;
		try {
			Method method = object.getClass().getMethod(methodName);
			Object obj = method.invoke(object) ;
			if(null != obj){
				valueSet=true;
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
		}
		return valueSet;
	}
	private static boolean isSet(Field field,Object object,List<Object> objects){
		boolean valueSet=false;
		try {
			Object obj = field.get(object);
			if(null != obj){
				valueSet=true;
				objects.add(obj);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				|  SecurityException e) {
		}
		return valueSet;
	}
	private static boolean isSet(Object object,Field field){
		boolean valueSet=false;
		try {
			Object obj = field.get(object);
			if(null != obj){
				valueSet=true;
			}
		} catch (IllegalAccessException | IllegalArgumentException
				|  SecurityException e) {
		}
		return valueSet;
	}
}
