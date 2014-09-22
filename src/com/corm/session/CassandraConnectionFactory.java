/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;

public class CassandraConnectionFactory {
	private static final Log log = LogFactory.getLog(CassandraConnectionFactory.class);
	private String [] serverNames= new String[]{"localhost"};
	private int serverPort=9042;
	private int readTimeoutMillisecond = 60000;
	private int batchCommitFactor = 500;
	//default consitency level - usually it is passed during operation call with ORMSession
	private ConsistencyLevel consistencyLevel=ConsistencyLevel.ONE;

	AuthProvider authProvider = null;
	PoolingOptions poolingOptions = null;
	SocketOptions socketOptions = null;
	
	
	public CassandraConnectionFactory() {
		socketOptions = new SocketOptions();
		socketOptions.setReadTimeoutMillis(this.readTimeoutMillisecond);
	}
	
	public void setAuthProvider(AuthProvider authProvider){
		this.authProvider = authProvider;
	}
	
	public void setAuthProvider(String user, String password){
		this.authProvider = new PlainTextAuthProvider(user,password);
	}
	
	public void setPoolingOptions(PoolingOptions poolingOptions){
		this.poolingOptions=poolingOptions;
	}
	public void setServerInfo(String[] servers, int portNumber){
		serverNames = servers;
		serverPort = portNumber;
	}
	
	public ConsistencyLevel getDefaultConsistencyLevel(){
		return this.consistencyLevel; 
	}
	public void setBatchCommitFactor(int batchCommitFactor){
		this.batchCommitFactor=batchCommitFactor;
	}
	
	public int getBatchCommitFactor(){
		return this.batchCommitFactor;
	}
	
	private volatile Cluster cluster;
	
	
	public void init(){
		setCluster();
	}
	
	/*
	 * Establish contact points in this case using localhost
	 */
	private synchronized void setCluster() {

		try {
			if (cluster == null) {
				log.debug("Creating cluster object......");
	
				Builder builder = new Cluster.Builder()
				.withSocketOptions(this.socketOptions)
				.addContactPoints(serverNames)
				.withPort(serverPort);
				
				if(null != this.authProvider){
					builder.withAuthProvider(authProvider);
				}
				if(null != this.poolingOptions){
					builder.withPoolingOptions(this.poolingOptions);
				}
				cluster =builder.build();
				
				Metadata metadata = cluster.getMetadata();
		        log.debug("Connected to cluster: "+metadata.getClusterName()+"\n");
		        for (Host host : metadata.getAllHosts()) {
		            log.debug("Datacenter: "+host.getDatacenter()+"; Host: "+host.getAddress()+"; Rack: "+host.getRack()+"\n");
		        }
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	public Session getSession(){
		return getSession("");
	}	
	public Session getSession(String keyspace){
		return cluster.connect(keyspace);
	}
	public void shutdown(Session session){
		if(null != session){
			session.close();
		}
	}
	public void shutdown(){
		if(null != cluster){
			cluster.close();
			cluster = null;
		}
	}
}
