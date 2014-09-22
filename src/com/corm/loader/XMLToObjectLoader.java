/*
* Copyright (C) 2014 Chandrasekar Sankarram - ckrams@yahoo.com.
*
* All rights reserved, can only reuse code with the copyright block of this section
* included in the copied source code including authors name and contact.
*
*/
package com.corm.loader;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XMLToObjectLoader {
	private static final Logger LOGGER = Logger.getLogger(XMLToObjectLoader.class.getName());
	@SuppressWarnings("unchecked")
	public static <T> T load(String filename,@SuppressWarnings("rawtypes") Class c) throws JAXBException{	
			 String str= XMLToObjectLoader.class.getResource(filename).getPath();

			LOGGER.info("File path resolved:"+str);		
			
			File file = new File(str);
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
	 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(file);
	}
}
