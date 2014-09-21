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

Detailed Usage overview : 

A. Define Cassandra KEYSPACE and COLUMN Family.

CREATE KEYSPACE orm WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

// define column family

  a. Simple data type

CREATE TABLE testsimple (
 storeId bigint,
 upcId bigint,
 maxUpcId bigint,
 PRIMARY KEY (storeId)
);

  b. Advanced data type

CREATE TABLE testmap (
 id int,
 description text,
 event_time timestamp,
 maps map<text, text>,
 tags set<text>,
 PRIMARY KEY (id)
);

B. Entity Bean definition

  a. Simple Type 
package com.corm.test.model;
import com.corm.annotations.Column;
import com.corm.Entity;
@ntity(columnFamily="testsimple", keyspace="orm")
public class TestSimpleTypes{

@ORMColumn(name = "storeId")
private Long storeId;
@ORMColumn(name = "upcId")
private Long upcId;
@ORMColumn(name = "maxUpcId")
private Long maxUpcId;
// getters and setters not shown but implied available
}

  b. Advanced Data types

package com.corm.test.model;

import com.corm.annotations.Column;
import com.corm.Entity;
@ntity(columnFamily="testmap", keyspace="orm")
public class TestAdvancedDataTypes{

@ORMColumn(name = "id")
Integer id;

@ORMColumn(name = "description")
String description ;

@ORMColumn(name = "event_time")
Date eventTime ;
@ORMColumn(name = "TAGS")
Set<String> tags ;
@ORMColumn(name = "MAPS")
Map<String,String> maps ;
// getters and setters not shown but implied available
}


C. Integration with ORM

 This code has dependency on the datastax core java libraries.

import com.corm.session.util.SessionUtil;
import com.corm.session.ORMSession;
// import dependencies for Entity bean 
import com.corm.test.model.TestSimpleTypes;
import com.corm.test.model.TestAdvancedDataTypes;

public class ORMDriverTest{
    private static String defaultKeySpace="orm";
private static String scanPath = "com.cassandra.orm.test.*";
private static String[] servers = new String[] {"localhost"};
private static int portNumber=9042;
public static void main(String args[]) throws Exception{
    // initialize the session factory
    SessionUtil.init(scanPath,servers,portNumber);
processSimpleTypes(defaultKeySpace);
    processAdvancedTypes(defaultKeySpace);
   
    // shutdown the session factory
SessionUtil.shutdown();
}
static void processSimpleTypes(String keySpace) throws Exception{
// acquire session
ORMSession session = SessionUtil.session(keySpace,3200);

List<TestSimpleTypes> list = new ArrayList<TestSimpleTypes>();

for(long i=1;i <=1000000;i++){
list.add(new TestSimpleTypes(i,i,i));
}
// persist the list using the ORMSession
session.create (list);
List<TestSimpleTypes> responses= session.retrieve(new TestSimpleTypes(),10) ; // limited to 10 for testing
    // evaluate the responses.
    ....
    
session.close();
}
static void processAdvancedTypes(String keySpace) throws Exception{
// acquire session
ORMSession session = SessionUtil.session(keySpace,4000);
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
t.setDescription("Test description "+i);
t.setEventTime(new Date());
t.setTags(tags);
t.setMaps(map);
list.add(t);
}
session.create (list);
session.close();
}
}

// source code and library will be uploaded soon
