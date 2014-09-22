/**
** Copyright (C) 2014 Chandrasekar Sankarram - Contact:cassandra.orm@gmail.com.
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*/

package com.corm.session;


import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.corm.db.ORMCriteria;
import com.datastax.driver.core.Session;

/**
*  
*  ORMSession is an entry point interface to participate with the Cassandra persistence
*  layer. This class offers all the CRUD operations needed to participate with cassandra
*  data store.
* @author Chandrasekar Sankarram - Contact:cassandra.orm@gmail.com
 */
public class ORMSession {
	private static final Logger LOGGER=Logger.getLogger(ORMSession.class.getName());
	ORMConnection connection=null;
	SessionFactory sessionFactory=null;

	
	ORMSession(ORMConnection connection,SessionFactory sessionFactory){
		this.connection=connection;
		this.sessionFactory = sessionFactory;
	}

    /**
     * Executes the supplied sql.
     *
     * @param sql Free form sql statement.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public void execute(String sql,SessionConsistencyLevel scLevel) throws SQLException{
		Session session =connection.getConnection();
		
		session.execute(sql);
	}
    /**
     * Creates the Object qualified as Cassandra Entity.
     *
     * @param object Object which is annotated as Entity.
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public <T> void create ( T object,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.create(object, connection,scLevel);

	}
    /**
     * Creates the List<Object> qualified as Cassandra Entity.
     *
     * @param objects List of Object which is annotated as Entity.
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public <T> void create ( List<T> objects,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.create(objects, connection,scLevel);

	}
    /**
     * Creates the List<Object> qualified as Cassandra Entity in asynchronus mode.
     *
     * @param objects List of Object which is annotated as Entity.
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public <T> void createAsync ( List<T> objects,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.createAsync(objects, connection,scLevel);

	}
    /**
     * Saves the Object qualified as Cassandra Entity.
     *
     * @param object Object which is annotated as Entity.
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public void save ( Object object,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.save(object, connection,scLevel);

	}
    /**
     * Saves the List<Object> qualified as Cassandra Entity in asynchronus mode.
     *
     * @param objects List of Object which is annotated as Entity.
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */
	public <T> void save ( List<T> objects,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.save(objects, connection,scLevel);

	}
	public void delete ( Object object,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.delete(object, connection,scLevel);

	}
    /**
     * delete the Object qualified as Cassandra Entity in asynchronus mode.
     *
     * @param object List of Object which is annotated as Entity.
     * @param criteria ORMCriteria object
     * @param scLevel SessionConsistencyLevel.
     * @exception throws SQLException - in case there is error in execution.
     */	
	public <T> void delete (T object, ORMCriteria criteria,SessionConsistencyLevel scLevel) throws SQLException{
		sessionFactory.delete(object, criteria,connection,scLevel);
	}
    /**
     * retrieve the Object qualified as Cassandra Entity.
     *
     * @param obj T is a generics object type which is Entity aware.
     * @param criteria ORMCriteria object
     * @return List<T> T returns the T type objects which is represents by the class name denoted by obj .
     * @exception throws SQLException - in case there is error in execution.
     */
	public <T> List<T> retrieve(T obj, ORMCriteria criteria) throws SQLException{
		return sessionFactory.retrieve(obj, criteria,connection);
	} 
    /**
     * retrieve the Object qualified as Cassandra Entity.
     *
     * @param obj T is a generics object type which is Entity aware.
     * @return List<T> T returns the T type objects which is represents by the class name denoted by obj .
     * @exception throws SQLException - in case there is error in execution.
     */
	public <T> List<T> retrieve(T obj) throws SQLException{
		return retrieve(obj,null);
	}
    /**
     * retrieve the Object qualified as Cassandra Entity.
     *
     * @param obj T is a generics object type which is Entity aware.
     * @param limit maximum number of records to limit the retrieve.
     * @return List<T> T returns the T type objects which is represents by the class name denoted by obj .
     * @exception throws SQLException - in case there is error in execution.
     */
	public <T> List<T> retrieve(T obj,int limit) throws SQLException{
		ORMCriteria criteria = new ORMCriteria(obj.getClass()); 
		criteria.limit(limit);
		return retrieve(obj,criteria);
	}
    /**
     * close the ORMSession and release the allocated connection resource.
     */	
	public void close(){
		if(null != connection){
			connection.close();
			connection = null;
		}
	}

	protected void finalize(){
		LOGGER.info("Finalizing and freeing resources");
		close();
	}
}
