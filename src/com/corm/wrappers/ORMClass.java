/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.wrappers;

import java.util.List;

import com.corm.mapping.generated.Class;
import com.corm.mapping.generated.Column;

public class ORMClass {

	private Class clazz;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public ORMClass(Class clazz){
		this.clazz=clazz;
		
	}

	
	public List<Column> properties(){
		return clazz.getColumn();
	}
	public String keyspace(){
		return clazz.getKeyspace();
	}
	
	public String table(){
		return clazz.getColumnFamily();
	}
	
	public String name(){
		return clazz.getName();
	}
}
