package org.araport.image.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NameMatchingUtils {
	
	
	private static final Log log = LogFactory
			.getLog(NameMatchingUtils.class);

	public static String getGermplasmNamebyFileName(final String fileName){
		String name = FilenameUtils.getBaseName(fileName);
		
		if (name.indexOf("_")!=-1){
			int firstIndex = name.indexOf("_");
			name = name.substring(0, firstIndex);
		}
		
		log.info("Source File Name: " + fileName + "Germplasm Name: " + name);
		
		return name;
		
	}
	
	
	public static void main (String[] args){
		
		String fileName = "11-1241-1_F3_01_H02_050311.JPG";
		
		String germplasmName = getGermplasmNamebyFileName(fileName);
		
		log.info("Source File: " + fileName + "GermplasmName: " + germplasmName);
	}
	
}
