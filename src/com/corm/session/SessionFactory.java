/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.session;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.corm.configuration.generated.Property;
import com.corm.configuration.generated.SessionFactoryLoader;
import com.corm.db.ORMCriteria;

public class SessionFactory {

	SessionFactoryLoader factory;
	CassandraConnectionFactory cassandraConnectionFactory;

	private static final Logger LOGGER = Logger
			.getLogger(SessionFactory.class.getName());
	private Map<String, String> mpProperties = new HashMap<String, String>();

	SessionFactory(SessionFactoryLoader factory,
			CassandraConnectionFactory cassandraConnectionFactory) {
		this.factory = factory;
		this.cassandraConnectionFactory = cassandraConnectionFactory;

		init();
	}
	
	private ORMConnection getConnection(String keySpace,int batchCommitFacor) {
		return new ORMConnection(
				cassandraConnectionFactory.getSession(keySpace),batchCommitFacor);
	}

	public ORMSession getSession(String keySpace) {
		return new ORMSession(getConnection(keySpace,cassandraConnectionFactory.getBatchCommitFactor()), this);
	}

	public ORMSession getSession(String keySpace,int batchCommitFactor) {
		return new ORMSession(getConnection(keySpace,batchCommitFactor), this);
	}
	
	private void init() {
		mpProperties.put("show_sql", getValue("show_sql"));
		String scanPath = factory.getScanPath();
		LOGGER.info("Scan Path:" + scanPath);
		if (null != scanPath) {
			LOGGER.info("Scanning and registering path:" + scanPath);
			ORMResourceRegistry.register(scanPath);
		}
		ORMResourceRegistry.register(factory.getMapping());

	}


	private String getValue(String key) {
		for (Property property : factory.getProperty()) {
			if (property.getName().equals(key)) {
				return property.getValue();
			}
		}

		return null;
	}

	void save(Object object, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		create(object, connection,scLevel);
	}

	void create(Object object, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		Profiler profiler = new Profiler();
		List<Object> objectsbind = new ArrayList<Object>();
		ORMSQLContext context = SQLStatementRegistry.getCreate( object,objectsbind);
		LOGGER.info("Using Save SQL:" + context.getSQL());
		PreparedStatement ps = connection.getConnection().prepare(
				context.getSQL());

		ps.setConsistencyLevel(scLevel.resolve());

		BoundStatement bs = ps.bind(objectsbind.toArray());

		connection.getConnection().execute(bs);

		objectsbind.clear();

		profiler.log(context.getSQL());
	}

	void createAsync(Object object, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		Profiler profiler = new Profiler();
		List<Object> objectsbind = new ArrayList<Object>();
		Session session=connection.getConnection();
		ORMSQLContext context = SQLStatementRegistry.getCreate( object,objectsbind);
		LOGGER.info("Using Save SQL:" + context.getSQL());
		PreparedStatement ps = session.prepare(
				context.getSQL());

		ps.setConsistencyLevel(scLevel.resolve());
		BoundStatement bs = ps.bind(objectsbind.toArray());

		session.executeAsync(bs);

		objectsbind.clear();

		profiler.log(context.getSQL());
	}

	<T> void save(List<T> objects, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		create(objects, connection,scLevel);
	}

	<T> void create(List<T> objects, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		Profiler profiler = new Profiler();
		Map<String, PreparedStatement> mpContextPrepared = new HashMap<String, PreparedStatement>();
		BatchStatement batch = new BatchStatement();
		int batchCommitFactor = connection.batchCommitFactor();
		LOGGER.info("Using batch Commit Factor of:" + batchCommitFactor);
		int i = 0;
		List<Object> objectsbind = new ArrayList<Object>();
		Session session=connection.getConnection();
		for (Object object : objects) {
			ORMSQLContext context = SQLStatementRegistry.getCreate( object,objectsbind);
			PreparedStatement ps = mpContextPrepared.get(context.getSQL());

			if (ps == null) {
				ps = session.prepare(context.getSQL());
				ps.setConsistencyLevel(scLevel.resolve());
				mpContextPrepared.put(context.getSQL(), ps);
			}
			BoundStatement bs = ps.bind(objectsbind.toArray());

			batch.add(bs);
			if (++i % batchCommitFactor == 0) {
				session.execute(batch);
				batch.clear();
			}
			objectsbind.clear();

		}

		if (i % batchCommitFactor != 0) {
			session.execute(batch);
			batch.clear();
		}
		objectsbind.clear();

		mpContextPrepared.clear();

		profiler.log("Batch processed in:");
	}

	<T> void createAsync(List<T> objects, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		Profiler profiler = new Profiler();
		Map<String, PreparedStatement> mpContextPrepared = new HashMap<String, PreparedStatement>();
		BatchStatement batch = new BatchStatement();
		int batchCommitFactor = connection.batchCommitFactor();
		LOGGER.info("Using batch Commit Factor of:" + batchCommitFactor);
		int i = 0;
		List<Object> objectsbind = new ArrayList<Object>();
		Session session=connection.getConnection();
		for (Object object : objects) {

			ORMSQLContext context = SQLStatementRegistry.getCreate( object,objectsbind);
			PreparedStatement ps = mpContextPrepared.get(context.getSQL());

			if (ps == null) {
				ps = session.prepare(context.getSQL());
				ps.setConsistencyLevel(scLevel.resolve());
				mpContextPrepared.put(context.getSQL(), ps);
			}
			BoundStatement bs = ps.bind(objectsbind.toArray());

			batch.add(bs);
			if (++i % batchCommitFactor == 0) {
				session.executeAsync(batch);
				batch.clear();
			}
			objectsbind.clear();

		}

		if (i % batchCommitFactor != 0) {
			connection.getConnection().execute(batch);
			batch.clear();
		}

		mpContextPrepared.clear();

		profiler.log("Batch processed in:");
	}

	void delete(Object object, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		Profiler profiler = new Profiler();
		String className = object.getClass().getName();
		List<Object> objectsbind = new ArrayList<Object>();
		ORMSQLContext context = SQLStatementRegistry.getDelete(
				ORMResourceRegistry.getClassName(className), object,objectsbind);
		LOGGER.info("Using Delete SQL:" + context.getSQL());
		PreparedStatement ps = connection.getConnection().prepare(
				context.getSQL());
		ps.setConsistencyLevel(scLevel.resolve());

		BoundStatement bs = ps.bind(objectsbind.toArray());

		connection.getConnection().execute(bs);
		bs = null;
		objectsbind.clear();
		objectsbind=null;
		profiler.log(context.getSQL());
	}

	<T> void delete(T obj, ORMCriteria criteria, ORMConnection connection,SessionConsistencyLevel scLevel)
			throws SQLException {
		//retrieve(obj, criteria, connection);
	}

	public <T> List<T> retrieve(T obj, ORMCriteria criteria,
			ORMConnection connection) throws SQLException {
		Profiler profiler = new Profiler();
		String sql = ORMCriteriaBuilder.build(obj, criteria);

		List<T> ret = retrieve(obj, connection, sql);

		profiler.log(sql);

		return ret;
	}

	private <T> List<T> retrieve(T obj, ORMConnection connection, String sql)
			throws SQLException {
		List<T> t = new ArrayList<T>();
		ResultSet rs = connection.getConnection().execute(sql);
		List<Row> rows =rs.all();// rs.iterator();

		for (Row row :rows) {
			t.add(bind(obj, row));
		}
		rs = null;
		
		rows.clear();
		rows = null;
		return t;
	}

	private static Object getObject(Row row, SQLTypes type, String columnName) {
		Object ret = null;
		switch (type) {
		case STRING:
			ret = row.getString(columnName);
			break;
		case INTEGER:
			ret = row.getInt(columnName);
			break;
		case DECIMAL:
			ret = row.getDecimal(columnName);
			break;
		// NULL,INTEGER,LONG,STRING,DATE,DOUBLE,DECIMAL,FLOAT,TIMESTAMP,BIGDECIMAL,BLOB,CLOB,LIST,MAP,SET,UUID,INET,BOOLEAN,BYTES,VARINT
		case VARINT:
			ret = row.getVarint(columnName);
			break;
		case DOUBLE:
			ret = row.getDouble(columnName);
			break;
		case LONG:
			ret = row.getLong(columnName);
			break;
		case BOOLEAN:
			ret = row.getBool(columnName);
			break;
		case UUID:
			ret = row.getUUID(columnName);
			break;
		case INET:
			ret = row.getInet(columnName);
			break;
		case BYTES:
			ret = row.getBytes(columnName);
			break;
		case DATE:
		case TIMESTAMP:
			ret = row.getDate(columnName);
			break;
		case LIST:
			ret = row.getList(columnName, java.lang.Object.class);
			break;
		case SET:
			ret = row.getSet(columnName, java.lang.Object.class);
			break;
		case MAP:
			ret = row.getMap(columnName, java.lang.Object.class,
					java.lang.Object.class);
			break;
		default:
			ret = null;

		}
		return ret;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> T bind(T obj, Row row) {
		com.corm.mapping.generated.Class clazz = ORMResourceRegistry
				.getClass(obj.getClass().getName());
		ClassDescriptor cd = ClassDescriptorRegistry.get(obj.getClass()
				.getName());
		List<com.corm.mapping.generated.Column> columns = clazz
				.getColumn();
		T t = null;
		try {
			t = (T) obj.getClass().newInstance();
			for (com.corm.mapping.generated.Column column : columns) {
				String columnName = column.getName();
				Object object = getObject(row, column.getType(), columnName);
				if (null != object) {
					cd.setValue(t, column.classField(), object);
				}
			}

		} catch (Exception e) {
		}
		return t;
	}

	void bindOutputParameter(CallableStatement cs, ValueObject object, int index)
			throws SQLException {

		switch (object.getType()) {
		case NULL:
			cs.setNull(index, object.getNullType());
			break;
		case INTEGER:
			cs.registerOutParameter(index, java.sql.Types.INTEGER);
			break;
		case DOUBLE:
		case DECIMAL:
			cs.registerOutParameter(index, java.sql.Types.DOUBLE);
			break;
		case FLOAT:
			cs.registerOutParameter(index, java.sql.Types.FLOAT);

			break;
		case LONG:
			cs.registerOutParameter(index, java.sql.Types.BIGINT);
			break;

		case STRING:
			cs.registerOutParameter(index, java.sql.Types.VARCHAR);
			break;
		case DATE:
			cs.registerOutParameter(index, java.sql.Types.DATE);
			break;
		case TIMESTAMP:
			cs.registerOutParameter(index, java.sql.Types.TIMESTAMP);
			break;
		default:
			break;

		}
	}

	void biindInputCallable(CallableStatement cs, ValueObject object, int index)
			throws SQLException {
		Object obj = object.get();
		switch (object.getType()) {
		case NULL:
			cs.setNull(index, object.getNullType());
			break;
		case INTEGER:
			cs.setInt(index, (Integer) obj);
			break;
		case DOUBLE:
		case DECIMAL:
			cs.setDouble(index, (Double) obj);
			break;
		case FLOAT:
			cs.setFloat(index, (Float) obj);
			break;
		case LONG:
			cs.setLong(index, (Long) obj);
			break;

		case STRING:
			cs.setString(index, (String) obj);
			break;
		case DATE:
			cs.setDate(index, new Date(((java.util.Date) obj).getTime()));
			break;
		case TIMESTAMP:
			cs.setTimestamp(index, (java.sql.Timestamp) obj);
			break;
		default:
			break;
		}
	}
}
