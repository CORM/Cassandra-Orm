/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

public class ORMLimit {

	int length=0;
	ORMLimit(int length){
		this.length=length;
	}
	
	public static ORMLimit value(int length){
		return new ORMLimit(length);
	}
	String clause(){
		return " limit "+this. length;
	}
}
