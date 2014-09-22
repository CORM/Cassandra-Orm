/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.logging.Logger;

import com.corm.configuration.generated.OrmConfiguration;
import com.corm.configuration.generated.SessionFactoryLoader;
import com.corm.loader.XMLToObjectLoader;

public class SessionFactoryManager{
	private static final Logger LOGGER=Logger.getLogger(SessionFactoryManager.class.getName());	
	private SessionFactory factory;
	private String configLocation;
	private String scanPath;
	private CassandraConnectionFactory cassandraConnectionFactory;
	public SessionFactoryManager(){
	}
	
	public void setConfigLocation(String configLocation){
		this.configLocation=configLocation;
	}
	public void setScanPath(String scanPath){ 
		this.scanPath=scanPath;
	}
	public void setCassandraConnectionFactory(CassandraConnectionFactory cassandraConnectionFactory){
		this.cassandraConnectionFactory=cassandraConnectionFactory;
	}
	
	public void init() {
		OrmConfiguration sessionFactory=null;
		try{
		 LOGGER.info("Loading Session Bootstrap configuration from:"+this.configLocation);
		 sessionFactory = load();
		 factory = new SessionFactory(sessionFactory.getSessionFactory(),this.cassandraConnectionFactory);		
		}catch (Exception e){
			LOGGER.info(e.getLocalizedMessage());
		}
	}
	private OrmConfiguration load(){
		OrmConfiguration sessionFactory=null;
		try{
			 LOGGER.info("Loading Session Bootstrap configuration from:"+this.configLocation);
			 sessionFactory = XMLToObjectLoader.load(this.configLocation, OrmConfiguration.class);
		}catch (Exception e){
			LOGGER.info(e.getLocalizedMessage());

			if(null == sessionFactory){
				sessionFactory = new OrmConfiguration();
				sessionFactory.setSessionFactory(new SessionFactoryLoader());
			}
			
			LOGGER.info("sessionFactory0:"+sessionFactory);
			SessionFactoryLoader sFactory = sessionFactory.getSessionFactory();
			
			if(null !=scanPath){
				String sPath = sFactory.getScanPath();
				if(null == sPath){
					sPath = "";
				}
				sPath += (sPath.length()>0)?",":"";
				sPath +=scanPath;
				sFactory.setScanPath(sPath);
			}
		}
		return sessionFactory;
	}
	public SessionFactory factory(){
		return factory;
	}
	
}
