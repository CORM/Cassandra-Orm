/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

public class Typeconvertor {

	public static Integer convertInteger(String object){
		if(null == object){
			return null;
		}

		try{
			return Integer.valueOf(object);
		}catch(Exception e){
			
		}
		return null;
	}

	public static Double convertDouble(String object){
		if(null == object){
			return null;
		}

		try{
			return Double.valueOf(object);
		}catch(Exception e){
			
		}
		return null;
	}
	
	public static Boolean convertBoolean(String object){
		if(null == object){
			return null;
		}

		try{
			return Boolean.valueOf(object);
		}catch(Exception e){
			
		}
		return null;
	}
}
