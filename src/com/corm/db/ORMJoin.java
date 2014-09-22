/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

public class ORMJoin {

	String entity;
	String localRef;
	String foreignRef;
	String onLeft;
	String onRight;
	String columns;
	String join;
	String localAlias;
	String foreignAlias;
	
	public ORMJoin(String entity, String localRef,String foreignRef, String join,String localAlias,String foreignAlias){
		this.join=join;
		this.localRef=localRef;
		this.foreignRef=foreignRef;
		this.entity=entity;
		this.localAlias=localAlias;
		this.foreignAlias=foreignAlias;
	}
	public String value(){
		
		return " " + join + " " +entity +" "+this.foreignAlias+ " ON " + localAlias+"."+localRef+"="+foreignAlias+"."+foreignRef  ;
	}
}
