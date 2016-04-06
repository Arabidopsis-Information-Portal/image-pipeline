package org.araport.image.network.download;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.common.ApplicationConstants;

public class FileLogger {

	private static final Log log = LogFactory
			.getLog(FileLogger.class);
	
	public static FileHandler handler = null;
	public static Logger errorLogger = null;
	
	private FileLogger(){
		
	}

	private static class FileLoggerHolder {
		public static final FileLogger INSTANCE = new FileLogger();
	}

	public static FileLogger getInstance() {
			return FileLoggerHolder.INSTANCE;
	}
	
	public static void initialize() throws SecurityException, IOException{
		
		log.info("Initializing File Logger...");
		
		handler = new FileHandler(ApplicationConstants.ERROR_LOG, true);
		errorLogger = Logger.getLogger("FileLogger");
		errorLogger.addHandler(handler);
		
	}
	
	public static Logger getErrorLogger() throws SecurityException, IOException{
		
		if (errorLogger==null){
			
			initialize();
		}
		
		return errorLogger;
	}
}
