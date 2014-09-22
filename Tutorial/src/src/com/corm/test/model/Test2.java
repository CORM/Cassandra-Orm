package com.corm.test.model;

import java.nio.ByteBuffer;
import java.util.Date;
//import java.util.Map;
//import java.util.Set;

import com.corm.annotations.Entity;
import com.corm.annotations.Column;

/*
CREATE TABLE test (
  id int,
  description TEXT,
  event_time timestamp,
  PRIMARY KEY (id)
);

 */
@Entity(columnFamily="testblob", keyspace="orm")
public class Test2 {

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ByteBuffer getBlobs() {
		return blobs;
	}

	public void setBlobs(ByteBuffer blobs) {
		this.blobs = blobs;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "id")		
	Integer id;
	@Column(name = "description")		
	String description;
	@Column(name = "blobs")	
	ByteBuffer blobs ;

	@Column(name = "event_time")	
	Date eventTime ;


}
