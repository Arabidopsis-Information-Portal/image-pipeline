package org.araport.image.listeners;

import org.apache.log4j.Logger;
import org.araport.image.domain.DbXref;
import org.araport.image.exception.ExceptionLogger;
import org.springframework.batch.core.listener.ItemListenerSupport;


public class ItemFailureLoggerListener extends ItemListenerSupport<Object, Object> {

	private static final Logger  log = Logger.getLogger(ItemFailureLoggerListener.class);
	
	public void onReadError(Exception ex) {
		
		ex.printStackTrace();
		
        log.error("Encountered error on read: " +  new ExceptionLogger(ex).getErrorMessage());
    }

    public void onWriteError(Exception ex, Object item) {
    	log.error("Encountered error on write: " +  new ExceptionLogger(ex, item).getErrorMessage());
    }

	
}
