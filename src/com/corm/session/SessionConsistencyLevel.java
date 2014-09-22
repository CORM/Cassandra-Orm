package com.corm.session;
import com.datastax.driver.core.ConsistencyLevel;

public enum SessionConsistencyLevel {
	 ANY,ONE,TWO,THREE,QUORUM,ALL,LOCAL_QUORUM,EACH_QUORUM,SERIAL,LOCAL_SERIAL,LOCAL_ONE;
	 
	public ConsistencyLevel resolve(){
		return ConsistencyLevel.valueOf(this.name());
	}
}
