/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class ClassDescriptor{
	private static final Logger LOGGER= Logger.getLogger(ClassDescriptor.class.getName());
	Map<String,String> mpGetters=new HashMap<String,String>();
	Map<String,Method> mpMethodGetters=new HashMap<String,Method>();
	
	Map<String,String> mpSetters=new HashMap<String,String>();
	Map<String,SQLTypes> mpSetterTypes=new HashMap<String,SQLTypes>();
	Map<String,SQLTypes> mpGetterTypes=new HashMap<String,SQLTypes>();
	Map<String,Field> mpFields=new HashMap<String,Field>();
//	Map<String,Integer> mpFieldHash=new HashMap<String,Integer>();
	Map<String,SQLTypes> mpFieldTypes=new HashMap<String,SQLTypes>();

	String catalog;
	String table;
	int hash;
	ClassDescriptor(String catalog,String table,String name){
		this.catalog=catalog;
		this.table=table;
		hash = name.hashCode();
		LOGGER.info("Generating class descriptor for:"+catalog+ "."+table);
	}
	int hash(){
		return hash;
	}
	String getCatalog(){
		return this.catalog;
	}
	String getTable(){
		return this.table;
	}
	void add(Class<?> descriptor,String key,SQLTypes type){
		String firstChar = key.substring(0, 1);
		String value = firstChar.toUpperCase()+key.substring(1);
		mpSetters.put(key,"set"+value);
		mpSetterTypes.put("set"+value,type);
		mpGetters.put(key,"get"+value);	
		mpGetterTypes.put("get"+value,type);
		setMethod2(descriptor,key);
	}

	String getMethod(String key){
		return mpGetters.get(key);
	}
	Field getField(String key){
		return mpFields.get(key);
	}	
	String setMethod(String key){
		return mpSetters.get(key);
	}
	
	
	Object getValue(Object object,String propName){
		Field field = getField(propName);
		return getMethodValue2(object,field);
	}

	public void setValue(Object object,String propName,Object value){

		Field field = getField(propName);
		
		setMethodValue2(object,field,value);
	}
	public void setValue(Object object,Field field,Object value){
		
		try {
			field.set(object,value);
		} catch (IllegalAccessException | IllegalArgumentException
				 | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new Object[] {}
		return;
	}	
	private Object getMethodValue2(Object object, Field field){
		Object obj =null;
		
		try {
			obj = field.get(object);
		} catch (IllegalAccessException | IllegalArgumentException
				| SecurityException e) {
		}
		return obj;
	}

	private void setMethodValue2(Object object, Field field,Object value){
		
		try {
			field.set(object,value);
		} catch (IllegalAccessException | IllegalArgumentException
				 | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new Object[] {}
		return;
	}	

    void setMethod2(Class<?> clazz,String name){

		try {
			Field field =clazz.getDeclaredField(name);
			if(null == field){
				field = clazz.getField(name);
			}else{
				field.setAccessible(true);
			}
//			mpFieldHash.put(name, name.hashCode());
			mpFields.put(name, field);
			
		} catch ( IllegalArgumentException
				 |  SecurityException | NoSuchFieldException e) {
		}
	}   
    Method method(String methodName){
    	return mpMethodGetters.get(methodName);
    }
}