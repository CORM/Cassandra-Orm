/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;


public class ValueObject {
	SQLTypes type=SQLTypes.NULL;
	int nullType=java.sql.Types.NULL;
	Object object;
	Object hiValue;
	Object loValue;
	String gaurd="'";
	
	public ValueObject(Object object,SQLTypes type){
		this.object=object;
		this.type=type;
//		if(null == object){
//			computeNullType(rawType);
//		}else{
//			computeType(rawType);
//		}
	}
	public ValueObject(Object hiValue,Object loValue,String rawType){
		this.object=hiValue;
		this.hiValue=hiValue;
		this.loValue=loValue;
		if(null == object){
			computeNullType(rawType);
		}else{
			computeType(rawType);
		}
	}
	public ValueObject(Object object){
		if(null != object){
			this.object=object;
			String rawType = object.getClass().getName();
			computeType(rawType);
		}
	}
	public Object getHigh(){
		return this.hiValue;
	}	
	public Object getLow(){
		return this.loValue;
	}	
	public Object get(){
		return object;
	}
	public boolean isNull(){
		return (null == object)?true:false;
	}
	
	public SQLTypes getType(){
		return type;
	}
	public String gaurd(){
		return this.gaurd;
	}
	public int getNullType(){
		return nullType;
	}
	private void computeNullType(String rawType){
		nullType= TypeResolver.getNullType(rawType);
	}
	private void computeType(String rawType){
		type = TypeResolver.getType(rawType);
		if((type== SQLTypes.INTEGER) || (type== SQLTypes.LONG)){
			gaurd="";
		}
	}
}
