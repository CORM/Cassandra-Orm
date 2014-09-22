/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.HashMap;
import java.util.Map;

import com.corm.wrappers.ORMClass;

public class ORMClassRegistry {
	private static Map<String,ORMClass> mpORMClass = new HashMap<String,ORMClass>();

	static void register(com.corm.mapping.generated.Class clazz){
		mpORMClass.put(clazz.getName(), new ORMClass(clazz));
	}
	
	static ORMClass get(String name){
		return mpORMClass.get(name);
	}
}
