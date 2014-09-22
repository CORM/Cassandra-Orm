package com.corm.session.util;

import java.util.ArrayList;
import java.util.List;

public class CallableParameterContainer {
	List<CallableParameter> parameters=new ArrayList<CallableParameter>();
	String procSpec;
	int status=-1; // undefined
	
	public CallableParameterContainer(String procSpec,List<CallableParameter> parameters){
		this.parameters=parameters;
		this.procSpec="{ call "+procSpec + "}";
	}
	
	public String procSpec(){
		return this.procSpec;
	}
	
	public int status(){
		return this.status;
	}
	public void status(int status){
		this.status=status;
	}
	
	public void add(CallableParameter bindParameter){
		this.parameters.add(bindParameter);
	}
	
	public List<CallableParameter> parameters(){
		return this.parameters;
	}
}
