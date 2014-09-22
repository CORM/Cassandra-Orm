Cassandra-Orm
=============

The Object Relational Mapping (ORM) driver helps bridge the impedance mismatch between relational world and the real life siutation which deals with object oriented modelling.

This driver addresses the main plumbing concerns associated with application integration with database and especially the new world of order that is no sql database.

Sneak Preview of ORM driver usage:

  1. Annotate Entity Bean - Example keyspace, table and field annotations
  2. Initialize SessionFactory
  3. Acquire Session and perform create ( either with a list of orm enitites or single object), or retrieve orm entites just   specifiying ORM entity object and also optionally pass in criteria's for retrieval
  4. Dispose session and shutdown sessionfactory

Please note: This ORM driver forwards the request to DataStax core java driver for performing interaction with CASSANDRA, and hence all the dependencies of DataStax java drivers must be satisified in the containing project. To use this ORM driver, knowledge and understanding of DataStax core java driver is not a requirement and this ORM driver defines a high level interface which obscures the necessity of knowing the DataStax core java driver. In short, from application interface view point, it is just recessary to understand the ORMSession interface. Bootstrapping requirements are abstracted within SessionUtil offered as part of ORM driver framework and at a bare minimum bootstrap just requires server list, port and the scan path for the entity beans. Entity beans can be defined in multiple packages and provided as comma separated string when defining scanPath.

Detailed Usage overview
=======================

  A. Define Cassandra KEYSPACE

    CREATE KEYSPACE orm WITH replication = {
      'class': 'SimpleStrategy',
      'replication_factor': '1'
    };
  
  B. Define Column Family

    a. Simple data type

    CREATE TABLE testsimple (
      id int,
      description TEXT,
      event_time timestamp,
      PRIMARY KEY (id)
    );

    b. Advanced data type

    CREATE TABLE testadvanced (
     id int,
     description text,
     event_time timestamp,
     maps map<text, text>,
     tags set<text>,
     PRIMARY KEY (id)
    );

  C. Entity Bean definition

    a. Simple Type 
  
      package com.corm.test.model;
      import com.corm.annotations.Column;
      import com.corm.Entity;
      @Entity(columnFamily="testsimple", keyspace="orm")
      public class TestSimpleTypes{
      
        @Column(name = "storeId")
        private Long storeId;
        @Column(name = "upcId")
        private Long upcId;
        @Column(name = "maxUpcId")
        private Long maxUpcId;
        // getters and setters not shown but implied available
      }

    b. Advanced Data types

    package com.corm.test.model;
    
    import com.corm.annotations.Column;
    import com.corm.Entity;
    @Entity(columnFamily="testadvanced", keyspace="orm")
    public class TestAdvancedDataTypes{
    
      @Column(name = "id")
      Integer id;
      
      @Column(name = "description")
      String description ;
      
      @Column(name = "event_time")
      Date eventTime ;
      @Column(name = "TAGS")
      Set<String> tags ;
      @Column(name = "MAPS")
      Map<String,String> maps ;
    // getters and setters not shown but implied available
    }

Connecting to Cassandra Cluster
===============================

The ORM framework offer SessionUtil which simplifies connect to Cassandra cluster as well as exposes interfaces to acquire ORMSession. Sample initialization code is shown below.

  a. Server and port Configuration initialization
    	
      static String[] servers = new String[] {"localhost"};
      static int portNumber=9042;
      static String scanPath="com.corm.test.model.*";
    	
  b. Connecting to Cassandra when no user/password credential is used.

      SessionUtil.init(scanPath, servers, portNumber);

  c. Connecting to Cassandra using credentials
      
      SessionUtil.setAuthProvider("myuser","mysecret");
      
      SessionUtil.init(scanPath, servers, portNumber);
      
  d. Connecting to Cassandra using AuthProvider - this can be any Datastax supported AuthProvider
      
      SessionUtil.setAuthProvider(authProvider);
      
      SessionUtil.init(scanPath, servers, portNumber);

Retrieving Data
===============

This ORM framework 2 overloaded methods fpr retrieval of data. The retrieve interface is described below.

  public <T> List<T> retrieve(T obj,int limit);
  
  public <T> List<T> retrieve(T obj, ORMCriteria criteria);
  
  ORMCriteria and details on how to use for building Criteria's for queries will be discussed later.
  
Inserting Data
==============

This ORM framework offers 2 overloaded methods to achieve either insert of single entity or a collection of idential entity objects.

  public void create ( Object object,SessionConsistencyLevel scLevel);
  
  public <T> void create ( List<T> object,SessionConsistencyLevel scLevel);


Sample Code in action
====================

 Please Note: This code has dependency on the datastax core java libraries.

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
  
    public class ORMTypesDriverTest {
        private static String defaultKeySpace="orm";
        private static String scanPath = "com.corm.test.model.*";
    	
    	public static void main(String args[]) throws Exception{
    		Bootstrapper.init(scanPath);
    		
            processSimple(defaultKeySpace);
        
            processAdvancedTypes(defaultKeySpace);
  
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
    }

Bootstrapper.java - just a wrapper for bootstrapping the connect to Cassandra cluster.

    package com.corm.test.app;
    
    import com.corm.session.util.SessionUtil;
    
    public class Bootstrapper {
    	private static String[] servers = new String[] {"localhost"};
    	private static int portNumber=9042;
    	
    	public static void init(String scanPath){
    		SessionUtil.init(scanPath, servers, portNumber);
    	}
    }

