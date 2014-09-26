package com.corm.test.model;

import java.util.Date;

import com.corm.annotations.Entity;
import com.corm.annotations.Column;
/*
CREATE TABLE testsimple (
  id int,
  description TEXT,
  event_time timestamp,
  PRIMARY KEY (id)
);

 */
@Entity(columnFamily="testsimple")
public class TestSimpleTypes {

	public TestSimpleTypes() {
		// TODO Auto-generated constructor stub
	}




	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




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



	@Column(name = "id")		
	Integer id;

	@Column(name = "description")	
	String description ;

	@Column(name = "event_time")	
	Date eventTime ;
}
