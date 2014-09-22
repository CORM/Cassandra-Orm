package com.corm.test.app;

import com.corm.session.util.SessionUtil;

public class Bootstrapper {
	private static String[] servers = new String[] {"localhost"};
	private static int portNumber=9042;
	
	public static void init(String scanPath){
		SessionUtil.init(scanPath, servers, portNumber);
	}
}
