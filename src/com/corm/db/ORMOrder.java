/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

public class ORMOrder {
	String column;
	String directive;
	int direction=0;
	ORMOrder(int direction,String column){
		this.column = column;
		this.direction=direction;
		if(direction ==0){
			directive = "ASC";
		}else{
			directive = "DESC";
		}
	}
	
	public static ORMOrder asc(String column){
		return new ORMOrder(0,column);
	}
	public static ORMOrder desc(String column){
		return new ORMOrder(1,column);
	}	
	public boolean isasc(){
		return direction ==0;
	}
	public boolean isdec(){
		return direction ==1;
	}
	
	public String raw(){
		return column;
	}
	public static String getOrderClause(){
		return " ORDER BY ";
	}
	
	String getClause(){
		return column +" "+ directive;
	}
}
