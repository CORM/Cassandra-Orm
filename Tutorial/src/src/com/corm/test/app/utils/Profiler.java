package com.corm.test.app.utils;

import java.io.Console;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
Profiler utility - handy to profile parts of functional unit
*/
public class Profiler{
	Console console = System.console();
	private static final Log log = LogFactory.getLog(Profiler.class);
	long start=0;
	long end =0;
	public Profiler(String str){
		start = System.currentTimeMillis();
		String out = new Date().toString()+" "+str;
		System.out.println(out);
	}
	public void init(){
		start = System.currentTimeMillis();
	}
	public void report(String preamble){
		end = System.currentTimeMillis();;
		long diff = end - start;
		long seconds = diff/1000;
		long microseconds = diff%1000;
//		diff /=1000;
		String str = new Date().toString()+" "+preamble+ " ,in "+seconds+"."+microseconds + " seconds";
		log.info(str);
		System.out.println(str);
//		console.printf (preamble+ diff + " seconds");
	}
	public void report(int records){
		end = System.currentTimeMillis();;
		long diff = end - start;
		double diffValue = end -start;
		long seconds = diff/1000;
		double throughput = (double)records / (diffValue/1000);
		long microseconds = diff%1000;
//		diff /=1000;
		String str = new Date().toString()+" "+"processed records:" +records +" ,in "+seconds+"."+microseconds + " seconds";
		str += " with throughput of "+ throughput +" per second";
		log.info(str);
		System.out.println(str);
//		console.printf (preamble+ diff + " seconds");
	}
}
