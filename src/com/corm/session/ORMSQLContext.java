/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.Set;

public class ORMSQLContext {

	private String sql;
	private Set<com.corm.mapping.generated.Column> columns;
	private Set<com.corm.mapping.generated.Column> injectable;

	ORMSQLContext(String sql,Set<com.corm.mapping.generated.Column> columns ){
		this.sql=sql;
		this.columns = columns;
	}
	boolean isInjectable(){
		return null != injectable && !injectable.isEmpty();
	}
		
	String getSQL(){
		return this.sql;
	}
	
	Set<com.corm.mapping.generated.Column> getColumn(){
		return this.columns;
	}
	
	Set<com.corm.mapping.generated.Column> getInjectable(){
		return this.injectable;
	}	
}
