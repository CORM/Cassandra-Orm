/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.logging.Logger;

public class Profiler {
	long start;
	private static final Logger LOGGER=Logger.getLogger(Profiler.class.getName());
	public Profiler(){
		start = System.currentTimeMillis();
	}
	
	public void log(String message){
		long delta = System.currentTimeMillis() - start;
		LOGGER.info(message+ " time taken:"+ delta + " msecs");
	}
	public void init(){
		start = System.currentTimeMillis();
	}

}
