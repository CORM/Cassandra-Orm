/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import com.datastax.driver.core.Session;


public class ORMConnection {
	Session session=null;
	int batchCommitFactor;

	ORMConnection(Session  session,int batchCommitFactor){
		this.session=session;
		this.batchCommitFactor=batchCommitFactor;
	}
	Session getConnection(){
		return this.session;
	}
	
	int batchCommitFactor(){
		return this.batchCommitFactor;
	}

	void close(){
		try {
			if(null !=session){
				session.close();
				session = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
