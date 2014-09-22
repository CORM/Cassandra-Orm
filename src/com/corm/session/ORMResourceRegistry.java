/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.corm.annotations.Entity;
import com.corm.configuration.generated.Mapping;
import com.corm.loader.XMLToObjectLoader;
import com.corm.mapping.generated.OrmMapping;
import com.corm.scanner.ORMAnnotationScanner;


public class ORMResourceRegistry {
	private static final Logger LOGGER= Logger.getLogger(ORMResourceRegistry.class.getName());
	private static Map<String,com.corm.mapping.generated.Class> mpResource=new HashMap<String,com.corm.mapping.generated.Class>();
	private static String ORMENTITY_TYPE = Entity.class.getName();//"com.corm.annotations.Entity";
	private static Map<String,Set<com.corm.mapping.generated.Column>> mpColumnSet = new HashMap<String,Set<com.corm.mapping.generated.Column>>();

	static void register(List<Mapping> mappings){
		for(Mapping resource:mappings){
			System.out.println("Resolved name:"+ORMENTITY_TYPE);
			OrmMapping ormMapping = getMapping(resource.getResource(),OrmMapping.class);
			mpResource.put(ormMapping.getClazz().getName(),ormMapping.getClazz());
			mpColumnSet.put(ormMapping.getClazz().getName(), prepare(ormMapping.getClazz()));
//			ClassDescriptorRegistry.register(ormMapping.getClazz().getName(),ormMapping.getClazz().getProperty());
			ClassDescriptorRegistry.register(ormMapping);
			SQLStatementRegistry.register(ormMapping.getClazz());
		}
	}  
	
	static List<OrmMapping> register(String scanPath){
		try{
			List<OrmMapping> mapping = ORMAnnotationScanner.scan(scanPath, ORMENTITY_TYPE);
			if(null != mapping){
				registerAnnotated(mapping);
				return mapping;
			}	
		}catch(Throwable e){
			LOGGER.info(e.getMessage());
		}
		return new ArrayList<OrmMapping>();
	}
	private static void registerAnnotated(List<OrmMapping> mappings){
		for(OrmMapping resource:mappings){
			mpResource.put(resource.getClazz().getName(),resource.getClazz());
			mpColumnSet.put(resource.getClazz().getName(), prepare(resource.getClazz()));
			ClassDescriptorRegistry.register(resource);
			SQLStatementRegistry.register(resource.getClazz());
		}
	}
	
	static String getClassName(String name){
		return getClass(name).getName();
	}
	
	static String getClassName(Object object){
		return getClass(object.getClass().getName()).getName();
	}
	static com.corm.mapping.generated.Class getClass(String className){
		return mpResource.get(className);
	}
	static List<com.corm.mapping.generated.Column> getColumnc(String className){
		return mpResource.get(className).getColumn();
	}
	static Set<com.corm.mapping.generated.Column> getColumnSet(String className){
		return mpColumnSet.get(className);
	}
	static ValueObject getValue(String className,Object object,String propName,SQLTypes type){
		ClassDescriptor cd = ClassDescriptorRegistry.get(className);
		Object obj= cd.getValue(object, propName);
		return new ValueObject(obj,type);
	}

	static void setValue(Object object,String propName,String type,String strategyHint,ORMConnection connection){
		ClassDescriptor cd = ClassDescriptorRegistry.get(object.getClass().getName());
		Object value=null;
		cd.setValue(object, propName,value);
	}

	
	private static OrmMapping getMapping (String resource,@SuppressWarnings("rawtypes") Class c){
		OrmMapping ormMapping = null;
		 try {
			 LOGGER.info("Resolved path for Resource Loading:"+resource);
			 ormMapping = XMLToObjectLoader.load(resource,c);

		} catch (JAXBException e) {
				e.printStackTrace();
		}
		return ormMapping;
	 
	}	  
	
	private static Set<com.corm.mapping.generated.Column> prepare(com.corm.mapping.generated.Class clazz){
		Set<com.corm.mapping.generated.Column> setColumns = new LinkedHashSet<com.corm.mapping.generated.Column>();
		for(com.corm.mapping.generated.Column column:clazz.getColumn()){
			setColumns.add(column);
		}
		return setColumns;	
	}
	
}
