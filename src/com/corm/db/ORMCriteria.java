/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORMCriteria {
	List<ORMCriterion> lsRestrictions=new ArrayList<ORMCriterion>();
	List<ORMOrder> lsOrder=new ArrayList<ORMOrder>();
	Map<String,List<ORMJoin>> mpJoins= new HashMap<String,List<ORMJoin>>();
	List<ORMAlias> lsAlias = new ArrayList<ORMAlias>();
	Map<String,List<ORMAlias>> mpAlias = new HashMap<String,List<ORMAlias>>();
	String limitClause="";
	String distinct="DISTINCT";
	boolean hasDistinct=false;
	
	String name;
	
	public String entity(){
		return name;
	}
	public ORMCriteria(Class<?> clazz){
		this.name=clazz.getName();
	}

	public ORMCriteria(Class<?> clazz,boolean hasDistinct){
		this.name=clazz.getName();
		this.hasDistinct=hasDistinct;
	}
	public boolean hasDistinct(){
		return this.hasDistinct;
	}
	
	public void add(ORMCriterion restriction){
		lsRestrictions.add(restriction);
	}
	public void add(ORMAlias alias){
		List<ORMAlias> list = mpAlias.get(alias.alias());
		if(null == list){
			list = new ArrayList<ORMAlias>();
			mpAlias.put(alias.alias(), list);
		}
		list.add(alias);
		lsAlias.add(alias);
	}	
	
	public void limit(int length){
		limitClause = ORMLimit.value(length).clause();
	}
	
	public String limit(){
		return this.limitClause;
	}
	public void add(ORMJoin join){
		List<ORMJoin> lsJoins = mpJoins.get(join.value());
		if( null == lsJoins){
			lsJoins = new ArrayList<ORMJoin>();
			mpJoins.put(join.value(),lsJoins);
		}
		lsJoins.add(join);
	}
	
	public void addOrder(ORMOrder order){
		lsOrder.add(order);
	}
	
	public List<ORMCriterion> criterions(){
		return lsRestrictions;
	}
	public List<ORMOrder> order(){
		return lsOrder;
	}
}

