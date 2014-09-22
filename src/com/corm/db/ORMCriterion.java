/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

public class ORMCriterion {
	String criterion;
	ORMCriterion(String criterion){
		this.criterion=criterion +" ";
	}
	public String get(){
		return this.criterion;
	}
}
