/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

public class ORMAlias {
	String clazz;
	String name;
	String alias;
	String simple;
	public ORMAlias(String clazz,String name,String alias){
		this.clazz=clazz;
		this.name=name;
		this.alias=alias;
		if(null !=clazz && !clazz.isEmpty()){
			int index = clazz.lastIndexOf(".");
			if(index > 0){
				simple = clazz.substring(index+1);
			}
		}
	}

	public String name(){
		return this.name;
	}
	public String simple(){
		return this.simple;
	}
	public String alias(){
		return this.alias;
	}
	
	public String value(){
		return simple+"."+ name + " AS " + alias;
	}

	public String valueSansAlias(){
		return simple+"."+ name ;
	}
	
	public String aggregateValue(String function){
		return function + "("+simple+"."+ name + ")"+" AS " + alias;
	}

}
