/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

import com.corm.session.ValueObject;

public class ORMRestriction {
	public static ORMCriterion eq(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion( column+ "="+gaurd+value.toString()+gaurd);
	}

	public static ORMCriterion gt(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion( column+ ">"+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion gte(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion( column+ ">="+gaurd+value.toString()+gaurd);
	}

	public static ORMCriterion lt(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion( column+ "<"+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion lte(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion( column+ "<="+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion notEq(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion(column+ "!="+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion like(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion(column+ " like " +gaurd  + value.toString()+gaurd);
	}
	public static ORMCriterion likeBefore(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion(column+ " like "+ gaurd+"%"  + value.toString()+gaurd);
	}
	public static ORMCriterion likeAfter(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion(column+ " like "+gaurd + value.toString()+"%" +gaurd);
	}
	public static ORMCriterion likeWild(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object value = object.get();
		return new ORMCriterion(column+  "like " +gaurd+"%" +value.toString()+"%"+gaurd);
	}
	
	public static ORMCriterion between(String column,Object lovalue,Object hivalue,String gaurd){
		return new ORMCriterion(column+  "between " +gaurd+ lovalue.toString()+"' " + "AND " +"'"+hivalue.toString()+gaurd);
	}	

	public static ORMCriterion eq(String column,Object value,String gaurd){
		return new ORMCriterion( column+ "="+gaurd+value.toString()+gaurd);
	}

	public static ORMCriterion gt(String column,Object value,String gaurd){
		return new ORMCriterion( column+ ">"+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion gte(String column,Object value,String gaurd){
		return new ORMCriterion( column+ ">="+gaurd+value.toString()+gaurd);
	}

	public static ORMCriterion lt(String column,Object value,String gaurd){
		return new ORMCriterion( column+ "<"+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion lte(String column,Object value,String gaurd){
		return new ORMCriterion( column+ "<="+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion notEq(String column,Object value,String gaurd){
		return new ORMCriterion(column+ "!="+gaurd+value.toString()+gaurd);
	}
	
	public static ORMCriterion like(String column,Object value,String gaurd){
		return new ORMCriterion(column+ " like " +gaurd  + value.toString()+gaurd);
	}
	public static ORMCriterion likeBefore(String column,Object value,String gaurd){
		return new ORMCriterion(column+ " like "+ gaurd+"%"  + value.toString()+gaurd);
	}
	public static ORMCriterion likeAfter(String column,Object value,String gaurd){
		return new ORMCriterion(column+ " like "+gaurd + value.toString()+"%" +gaurd);
	}
	public static ORMCriterion likeWild(String column,Object value,String gaurd){
		return new ORMCriterion(column+  "like " +gaurd+"%" +value.toString()+"%"+gaurd);
	}
	
	public static ORMCriterion between(String column,ValueObject object){
		String gaurd = object.gaurd();
		Object lovalue= object.getLow();
		Object hivalue = object.getHigh();
		return new ORMCriterion(column+  "between " +gaurd+ lovalue.toString()+"' " + "AND " +"'"+hivalue.toString()+gaurd);
	}
	
	public static ORMCriterion isNull(String column){
		return new ORMCriterion(column+  " is null");
	}
	public static ORMCriterion notNull(String column){
		return new ORMCriterion(column+  " is not null");
	}
}
