/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.scanner;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.corm.annotations.Column;
import com.corm.annotations.Entity;
import com.corm.mapping.generated.ObjectFactory;
import com.corm.mapping.generated.OrmMapping;
import com.corm.session.SQLTypes;
import com.corm.session.TypeResolver;



public class ORMAnnotationScanner {
	private static final Logger LOGGER=Logger.getLogger(ORMAnnotationScanner.class.getName());
	private static String delimiter= "," ;	

	public static List<OrmMapping> scan(String basePath,String type) throws Throwable{		
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    Set<String> set = tokens(basePath,delimiter);
	    Map<String,String> mpRefEntity= new HashMap<String,String>();
	    for(String path:set){
		    String cleansedPath = path.replace('.', '/');
		    boolean recurse=(cleansedPath.indexOf("*") >=0 )?true:false;
		    if(recurse){
		    	int index = cleansedPath.indexOf("*");
		    	cleansedPath = cleansedPath.substring(0,index-1);
		    }
		    String packageName = cleansedPath.trim();//.replace('/', '.');
		    Enumeration<URL> resources = classLoader.getResources(packageName);
		    List<File> dirs = new ArrayList<File>();
		    while (resources.hasMoreElements())
		    {
		        URL resource = resources.nextElement();
		        dirs.add(new File(resource.getFile()));
		        if(!recurse){
		        	break;
		        }
		    }
		    
		    for (File directory : dirs)
		    {
		        classes.addAll(findClasses(directory,packageName,type,mpRefEntity));
		    }
	    }
	    return build(classes,mpRefEntity);
	}
	
	private static Set<String> tokens(String in,String delimiters){
		Set<String> tokenized= new LinkedHashSet<String>();
		

		StringTokenizer tokenizer =  new StringTokenizer(in,delimiters);
		while(tokenizer.hasMoreTokens()){
			String stripped = tokenizer.nextToken().trim();
			if(!stripped.isEmpty()){
				tokenized.add(stripped);
			}
		}

		return tokenized;
	}
	@SuppressWarnings("rawtypes")
	private static List<Class<?>> findClasses(File directory, String packageName,String type, Map<String,String> entityRef) throws ClassNotFoundException
	{
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    if (!directory.exists())
	    {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files)
	    {
	        if (file.isDirectory())
	        {
	            classes.addAll(findClasses(file, packageName + "." + file.getName(), type,entityRef));
	        }
	        else if (file.getName().endsWith(".class"))
	        {
	        	String classConformingPath =packageName.replace('/', '.');;
	        	Class clazz = Class.forName(classConformingPath + '.' + file.getName().substring(0, file.getName().length() - 6));
	        	Annotation[] annots = clazz.getDeclaredAnnotations();
	        	for(Annotation annotation:annots){
	        		if(annotation.annotationType().getName().equals(type)){
	        			classes.add(clazz);
	        			String entity=getFieldAnnotationValue(annotation,"keyspace");
	        			String table=getFieldAnnotationValue(annotation,"columnFamily");
	        			String value = entity+"."+table;
	        			entityRef.put(clazz.getName(), value);
	        		}
	        	}        
	        }
	    }
	    return classes;
	}
	
	@SuppressWarnings("rawtypes")
	static List<OrmMapping> build(List<Class<?>> classes, Map<String,String> entityRef) throws ClassNotFoundException{
		List<OrmMapping> mappings = new ArrayList<OrmMapping>();
		for(Class clazz:classes){
			LOGGER.info("Discovered annotation for:"+clazz.getName());
			mappings.add(build(clazz,entityRef));
		}		
		return mappings;
	}
	static OrmMapping build(Class<?> parentClass, Map<String,String> entityRef) throws ClassNotFoundException{
		ObjectFactory factory = new ObjectFactory();
		OrmMapping mapping = factory.createOrmMapping();
		com.corm.mapping.generated.Class clazz = factory.createClass();
		String catalog=getClassAnnotationValue(parentClass,Entity.class,"keyspace");
		String table=getClassAnnotationValue(parentClass,Entity.class,"columnFamily");
		clazz.setName(parentClass.getName());
		mapping.setClazz(clazz);
		clazz.setName(parentClass.getName());
		clazz.setKeyspace(catalog);
		clazz.setTable(table);
		List<com.corm.mapping.generated.Column> columns = clazz.getColumn();
		
		Field[] fields = parentClass.getDeclaredFields();
		for(Field field:fields){
			String fieldName=field.getName();

			Annotation annotation=field.getAnnotation(Column.class);
			if(annotation !=null){
				com.corm.mapping.generated.Column column= factory.createColumn();
				columns.add(column);
				column.setName((String)getFieldAnnotationValue(annotation,"name"));
				column.setField(fieldName);
				SQLTypes type = getFieldAnnotationValue(annotation,"type");
				if(SQLTypes.NULL== type){
					// resolve and bind new sql type 
					Class<?> fieldType = field.getType();
					SQLTypes sqlType = TypeResolver.getType(fieldType.getSimpleName());
					column.setType(sqlType);
				}
				column.classField(field(parentClass,fieldName));
				column.hash(fieldName.hashCode());
				column.setIgnoreFor((String)getFieldAnnotationValue(annotation,"ignoreFor"));
				column.setUnique((Boolean)getFieldAnnotationValue(annotation,"unique"));
				column.setNullable((Boolean)getFieldAnnotationValue(annotation,"notNull"));
				column.setRawType((String)getFieldAnnotationValue(annotation,"rawType"));
//				if((Integer)getFieldAnnotationValue(annotation,"length") != -1){
//					column.setLength((Integer)getFieldAnnotationValue(annotation,"length"));
//				}
				column.setHint((String)getFieldAnnotationValue(annotation,"hint"));			
				column.setIgnoreFor((String)getFieldAnnotationValue(annotation,"ignoreFor"));
				column.setInject((Boolean)getFieldAnnotationValue(annotation,"inject"));
			}
		}
		return mapping;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T>T getClassAnnotationValue(Class<?> classType, Class annotationType, String attributeName) {
        T value = null;
 
		Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null) {
            try {
                value = (T) annotation.annotationType().getMethod(attributeName).invoke(annotation);
            } catch (Exception ex) {
            }
        }
 
        return value;
    }
	@SuppressWarnings("unchecked")
	private static <T>T getFieldAnnotationValue(Annotation annotation, String attributeName) {
        T value = null;
 
        if (annotation != null) {
            try {
                value = (T) annotation.annotationType().getMethod(attributeName).invoke(annotation);
            } catch (Exception ex) {
            }
        }
 
        return value;
    }
	
    private static Field field(Class<?> clazz,String name){
    	Field field=null;
		try {
			field =clazz.getDeclaredField(name);
			if(null == field){
				field = clazz.getField(name);
			}else{
				field.setAccessible(true);
			}
			
		} catch ( IllegalArgumentException
				 |  SecurityException | NoSuchFieldException e) {
		}
		return field;
	}   
  
}
