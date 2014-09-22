package com.corm.session.util;

import com.corm.session.ValueObject;

public class CallableParameter {
	Class<?> type;
	Object value;
	int direction; // 0 in, 1 out
	int position;
	String name;
	ValueObject valueObject;
	
	public CallableParameter(String name, Object value,int position,int direction){
		valueObject = new ValueObject(value);
		this.value=value;
		this.position=position;
		this.direction=direction;
		this.name =name;
	}
	public String name(){
		return this.name;
	}
	
	public Class<?> type() {
		return type;
	}
	public void type(Class<?> type) {
		this.type = type;
	}
	public Object value() {
		return value;
	}
	public void value(Object value) {
		this.value = value;
	}
	public int direction() {
		return direction;
	}
	public void direction(int direction) {
		this.direction = direction;
	}
	public int position() {
		return position;
	}
	public void position(int position) {
		this.position = position;
	}
	
	public ValueObject valueObject(){
		return this.valueObject;
	}
	
}
