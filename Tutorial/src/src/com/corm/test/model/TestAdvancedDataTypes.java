package com.corm.test.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.corm.annotations.Entity;
import com.corm.annotations.Column;

/*
CREATE TABLE testadvanced (
 id int,
 description text,
 event_time timestamp,
 maps map<text, text>,
 tags set<text>,
 PRIMARY KEY (id)
);

 */
@Entity(columnFamily="testadvanced", keyspace="orm")
public class TestAdvancedDataTypes {

	public TestAdvancedDataTypes() {
		// TODO Auto-generated constructor stub
	}


	@Column(name = "id")		
	Integer id;

	@Column(name = "description")	
	String description ;

	@Column(name = "event_time")	
	Date eventTime ;
	@Column(name = "TAGS")	
	Set<String> tags ;
	@Column(name = "MAPS")	
	Map<String,String> maps ;




	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}





	public Integer getId() {
		return id;
	}





	public void setId(Integer id) {
		this.id = id;
	}





	public Set<String> getTags() {
		return tags;
	}





	public void setTags(Set<String> tags) {
		this.tags = tags;
	}





	public Map<String, String> getMaps() {
		return maps;
	}





	public void setMaps(Map<String, String> maps) {
		this.maps = maps;
	}



}
