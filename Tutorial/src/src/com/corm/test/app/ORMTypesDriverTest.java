package com.corm.test.app;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.corm.session.ORMSession;
import com.corm.session.SessionConsistencyLevel;
import com.flights.app.DataAcquirer;
import com.corm.session.util.SessionUtil;
import com.corm.test.app.utils.Profiler;
import com.corm.test.model.TestSimpleTypes;
import com.corm.test.model.TestAdvancedDataTypes;
import com.corm.test.model.Test2;

public class ORMTypesDriverTest {

    private static String defaultKeySpace="orm";
    private static String scanPath = "com.corm.test.model.*";
	public static void main(String args[]) throws Exception{
		Bootstrapper.init(scanPath);
		processSimple(defaultKeySpace);
		processAdvancedTypes(defaultKeySpace);
		// test byte buffer data type - this needs changes to filename in the BlobFetcher class
//		processBlob("orm");
		SessionUtil.shutdown();
	}
	
	static void processSimple(String keySpace) throws Exception{
		// acquire session
		ORMSession session = SessionUtil.session(keySpace,3500);	


		List<TestSimpleTypes> list = new ArrayList<TestSimpleTypes>();

		for(int i=1;i <=1000000;i++){
			TestSimpleTypes t= new TestSimpleTypes();
			t.setId(i);
			t.setDescription("Test description " + i);
			t.setEventTime(new Date());

			list.add(t);
		}
		
		Profiler profiler = new Profiler("Starting processing");
		session.create (list,SessionConsistencyLevel.ONE);

		profiler.report(list.size());
		session.close();
	}	
	
	static void processAdvancedTypes(String keySpace) throws Exception{
		// acquire session
		ORMSession session = SessionUtil.session(keySpace,500);	

		Set<String> tags = new HashSet<String>();
		tags.add("Test1");
		tags.add("Test2");
		tags.add("Test3");
		tags.add("Test1");
		Map<String,String> map = new HashMap<String,String>();
		map.put("Test1", "Test1");
		map.put("Test2", "Test2");
		map.put("Test3", "Test5");
		List<TestAdvancedDataTypes> list = new ArrayList<TestAdvancedDataTypes>();

		for(int i=1;i <=1000000;i++){
			TestAdvancedDataTypes t= new TestAdvancedDataTypes();
			t.setId(i+15);
			t.setDescription("Test description");
			t.setEventTime(new Date());
			t.setTags(tags);
			t.setMaps(map);
			list.add(t);
		}
		
		Profiler profiler = new Profiler("Starting processing");
		session.create (list,SessionConsistencyLevel.ONE);

		profiler.report(list.size());
		session.close();
	}	
	
	static void processBlob(String keySpace) throws Exception{
		
		DataAcquirer da = new DataAcquirer("");

		List<Test2> list = da.getBlobs();
		Profiler profiler = new Profiler("Starting processing");

		profiler.report("Time taken for acquiring source:"+list.size()+ " records ");
		// acquire session
		ORMSession session = SessionUtil.session(keySpace,12);	
		profiler = new Profiler("Starting process dump to casssandra:");


		session.create(list,SessionConsistencyLevel.ONE);
		profiler.report(list.size());

		session.close();
	}
}

