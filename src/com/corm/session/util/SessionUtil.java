/**
** Copyright (C) 2014 Chandrasekar Sankarram - Contact:cassandra.orm@gmail.com.
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*/

package com.corm.session.util;

import com.corm.session.CassandraConnectionFactory;
import com.corm.session.SessionFactory;
import com.corm.session.ORMSession;
import com.corm.session.SessionFactoryManager;
import com.datastax.driver.core.AuthProvider;

/**
*  
*  SessionUtil is a utility class which simplifies the cassandra cluster setup as well as bootstraps the ORM framework.
* @author Chandrasekar Sankarram - Contact:cassandra.orm@gmail.com
 */
public class SessionUtil {

	static ORMSession session=null;

	
	private static volatile SessionFactory factory;
	private static CassandraConnectionFactory cassandraConnectionFactory;

	private static AuthProvider _provider = null;
	private static String _user=null;
	private static String _password=null;
	
	public static void setAuthProvider(String user,String password){
		_user=user;
		_password=password;
	}
	public static void setAuthProvider(AuthProvider provider){
		_provider = provider;
	}
	/*
	 * session(String keyspace) - overloaded call with ad hoc keyspace
	 */
	public static ORMSession session(String keyspace){
		return factory.getSession(keyspace);
	}
	/*
	 * session(String keyspace,int batchCommitFactor) - overloaded call with overrideable parameters
	 */
	public static ORMSession session(String keyspace,int batchCommitFactor){
		return factory.getSession(keyspace,batchCommitFactor);
	}
	public static void shutdown(){
		cassandraConnectionFactory.shutdown();	
	}
    /**
     * Initialize the session factory by performing scan and discovery of ORM participating entity .
     *
     * @param scanPath Path of entity aware beans usually expressed as package.*" and multiple paths separated by , 
     * @param servers contact points for Cassandra nodes.
     * @param portNumber Cassandra node port number. 
      */		
	public static void init(String scanPath,String[] servers,int portNumber) {

		try{
			 SessionFactoryManager sessionFactoryManager = new SessionFactoryManager();
			 sessionFactoryManager.setScanPath(scanPath);
			 cassandraConnectionFactory = new CassandraConnectionFactory();
			 
			 if(null !=_user && null !=_password){
				 cassandraConnectionFactory.setAuthProvider(_user, _password);
			 }else if ( null != _provider){
				 cassandraConnectionFactory.setAuthProvider(_provider);
			 }
			 
			 cassandraConnectionFactory.setServerInfo(servers, portNumber);

			 cassandraConnectionFactory.init();
			 sessionFactoryManager.setCassandraConnectionFactory(cassandraConnectionFactory);
	
			 sessionFactoryManager.init();
			 factory = sessionFactoryManager.factory();
			 

		}catch(Exception e){
			
		}
	}

}
