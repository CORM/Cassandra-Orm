/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TypeResolver {
	private static Map<String, SQLTypes> mpTypes = new HashMap<String,SQLTypes>();
	private static Map<String,Integer> mpNullTypes = new HashMap<String,Integer>();
	private static Map<String,Class<?>> mpParamTypes = new HashMap<String,Class<?>>();

	static{
		mpTypes.put("String", SQLTypes.STRING);
		mpTypes.put("java.lang.String", SQLTypes.STRING);
		mpTypes.put("string", SQLTypes.STRING);
		mpTypes.put("NVARCHAR", SQLTypes.STRING);
		mpTypes.put("nvarchar", SQLTypes.STRING);
		mpTypes.put("VARCHAR", SQLTypes.STRING);
		mpTypes.put("varchar", SQLTypes.STRING);
		mpTypes.put("Integer", SQLTypes.INTEGER);
		mpTypes.put("int", SQLTypes.INTEGER);
		mpTypes.put("java.lang.Integer", SQLTypes.INTEGER);
		mpTypes.put("java.lang.Long", SQLTypes.LONG);
		mpTypes.put("long", SQLTypes.LONG);
		mpTypes.put("date", SQLTypes.DATE);
		mpTypes.put("Date", SQLTypes.DATE);
		mpTypes.put("java.util.Date", SQLTypes.DATE);
		mpTypes.put("java.sql.Date", SQLTypes.DATE);
		mpTypes.put("java.sql.Blob", SQLTypes.BLOB);
		mpTypes.put("java.sql.Clob", SQLTypes.CLOB);
		
		mpTypes.put("Timestamp", SQLTypes.TIMESTAMP);
		mpTypes.put("BIGDECIMAL", SQLTypes.BIGDECIMAL);
		mpTypes.put("BigDecimal", SQLTypes.BIGDECIMAL);
		mpTypes.put("timestamp", SQLTypes.TIMESTAMP);
		mpTypes.put("java.sql.Timestamp", SQLTypes.TIMESTAMP);
		
		mpTypes.put("float", SQLTypes.FLOAT);
		mpTypes.put("java.lang.Float", SQLTypes.FLOAT);
		mpTypes.put("double", SQLTypes.DOUBLE);
		mpTypes.put("java.lang.Double", SQLTypes.DOUBLE);
		mpTypes.put("decimal", SQLTypes.DECIMAL);
		mpTypes.put("java.lang.Decimal", SQLTypes.DECIMAL);

		mpTypes.put("java.math.BigDecimal", SQLTypes.BIGDECIMAL);
		mpTypes.put("java.math.BigInteger", SQLTypes.VARINT);
		mpTypes.put("java.util.Map", SQLTypes.MAP);
		mpTypes.put("java.util.Set", SQLTypes.SET);
		mpTypes.put("java.util.List", SQLTypes.LIST);
		mpTypes.put("java.util.UUID", SQLTypes.UUID);
		mpTypes.put("java.net.InetAddress", SQLTypes.INET);
		mpTypes.put("java.nio.ByteBuffer", SQLTypes.BYTES);
		mpTypes.put("java.nio.CharBuffer", SQLTypes.CLOB);		

		mpTypes.put("BigDecimal", SQLTypes.BIGDECIMAL);
		mpTypes.put("BigInteger", SQLTypes.VARINT);
		mpTypes.put("Map", SQLTypes.MAP);
		mpTypes.put("Set", SQLTypes.SET);
		mpTypes.put("List", SQLTypes.LIST);
		mpTypes.put("UUID", SQLTypes.UUID);
		mpTypes.put("InetAddress", SQLTypes.INET);
		mpTypes.put("ByteBuffer", SQLTypes.BYTES);
		mpTypes.put("CharBuffer", SQLTypes.CLOB);	
		
		mpNullTypes.put("Date", java.sql.Types.DATE);
		mpNullTypes.put("date", java.sql.Types.DATE);
		mpNullTypes.put("java.util.Date", java.sql.Types.DATE);
		mpNullTypes.put("java.sql.Date", java.sql.Types.DATE);

		mpNullTypes.put("Timestamp", java.sql.Types.TIMESTAMP);
		mpNullTypes.put("timestamp", java.sql.Types.TIMESTAMP);
		mpNullTypes.put("java.sql.Timestamp", java.sql.Types.TIMESTAMP);
		
		mpNullTypes.put("int", java.sql.Types.INTEGER);
		mpNullTypes.put("INT", java.sql.Types.INTEGER);
		mpNullTypes.put("Integer", java.sql.Types.INTEGER);
		mpNullTypes.put("INTEGER", java.sql.Types.INTEGER);
		mpNullTypes.put("java.lang.Integer", java.sql.Types.INTEGER);
		
		
		mpNullTypes.put("long", java.sql.Types.INTEGER);
		mpNullTypes.put("Long", java.sql.Types.INTEGER);
		mpNullTypes.put("LONG", java.sql.Types.INTEGER);
		mpNullTypes.put("java.lang.Long", java.sql.Types.INTEGER);		
		
		mpNullTypes.put("float", java.sql.Types.FLOAT);
		mpNullTypes.put("Float", java.sql.Types.FLOAT);
		mpNullTypes.put("FLOAT", java.sql.Types.FLOAT);
		mpNullTypes.put("java.lang.Float", java.sql.Types.FLOAT);	

		mpNullTypes.put("double", java.sql.Types.DOUBLE);
		mpNullTypes.put("Double", java.sql.Types.DOUBLE);
		mpNullTypes.put("DOUBLE", java.sql.Types.DOUBLE);
		mpNullTypes.put("java.lang.Double", java.sql.Types.DOUBLE);

		mpNullTypes.put("decimal", java.sql.Types.DECIMAL);
		mpNullTypes.put("Decimal", java.sql.Types.DECIMAL);
		mpNullTypes.put("DECIMAL", java.sql.Types.DECIMAL);
		mpNullTypes.put("java.lang.Decimal", java.sql.Types.DECIMAL);
		
		mpNullTypes.put("NVARCHAR", java.sql.Types.NVARCHAR);
		mpNullTypes.put("nvarchar", java.sql.Types.NVARCHAR);
		
		mpNullTypes.put("CHAR", java.sql.Types.CHAR);
		mpNullTypes.put("NCHAR", java.sql.Types.NCHAR);
		mpNullTypes.put("java.sql.Blob", java.sql.Types.BLOB);
		
		mpNullTypes.put("java.math.BigDecimal", java.sql.Types.DOUBLE);
		mpNullTypes.put("java.math.BigInteger", java.sql.Types.VARBINARY);
		mpNullTypes.put("java.util.Map",java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.util.Set", java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.util.List", java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.util.UUID", java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.net.InetAddress", java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.nio.ByteBuffer", java.sql.Types.JAVA_OBJECT);
		mpNullTypes.put("java.nio.CharBuffer", java.sql.Types.JAVA_OBJECT);

//	NULL,INTEGER,LONG,STRING,DATE,DOUBLE,DECIMAL,FLOAT,TIMESTAMP,BIGDECIMAL,BLOB,CLOB,LIST,MAP,SET,UUID,INET,BOOLEAN,BYTES,VARINT
		mpParamTypes.put("java.math.BigDecimal", BigDecimal.class);
		mpParamTypes.put("java.math.BigInteger", BigInteger.class);
		mpParamTypes.put("java.util.Map", Map.class);
		mpParamTypes.put("java.util.Set", Set.class);
		mpParamTypes.put("java.util.List", List.class);
		mpParamTypes.put("java.util.UUID", UUID.class);
		mpParamTypes.put("java.net.InetAddress", InetAddress.class);
		mpParamTypes.put("java.nio.ByteBuffer", ByteBuffer.class);
		mpParamTypes.put("java.nio.CharBuffer", CharBuffer.class);
		mpParamTypes.put("String", String.class);
		mpParamTypes.put("NVARCHAR", String.class);
		mpParamTypes.put("VARCHAR", String.class);
		mpParamTypes.put("string", String.class);
		mpParamTypes.put("java.lang.String", String.class);
		mpParamTypes.put("Integer", Integer.class);
		mpParamTypes.put("int", Integer.class);
		mpParamTypes.put("java.lang.Integer", Integer.TYPE);
		mpParamTypes.put("java.lang.Long", Long.class);
		mpParamTypes.put("long", Long.TYPE);
		mpParamTypes.put("date", Date.class);
		mpParamTypes.put("Date", Date.class);
		mpParamTypes.put("java.util.Date", Date.class);
		mpParamTypes.put("java.sql.Date", Date.class);
		
		mpParamTypes.put("float", Float.TYPE);
		mpParamTypes.put("java.lang.Float", Float.TYPE);
		mpParamTypes.put("double", Double.TYPE);
		mpParamTypes.put("java.lang.Double", Double.TYPE);
		mpParamTypes.put("decimal", Double.TYPE);
		mpParamTypes.put("java.lang.Decimal", Double.TYPE);
		mpParamTypes.put("java.sql.Blob", java.sql.Blob.class);
		mpParamTypes.put("Blob", java.sql.Blob.class);
		
		mpParamTypes.put("Timestamp", Timestamp.class);
		mpParamTypes.put("timestamp", Timestamp.class);
		mpParamTypes.put("java.sql.Timestamp", Timestamp.class);
		mpParamTypes.put("BIGDECIMAL", BigDecimal.class);
		mpParamTypes.put("java.math.BigDecimal", BigDecimal.class);
		
	}
	
	public static SQLTypes getType(String type){
		return mpTypes.get(type);
	}
	
	static int getNullType(String type){
		return mpNullTypes.get(type);
	}
	public static Class<?> getParamType(String type){
		return mpParamTypes.get(type);
	}
}
