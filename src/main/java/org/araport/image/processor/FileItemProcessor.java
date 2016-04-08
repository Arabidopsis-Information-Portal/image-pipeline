package org.araport.image.processor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.araport.image.dataloader.DataService;
import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.network.download.FileProcessor;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor <String, DatabaseFileImage>{

	private static final Logger log = Logger.getLogger(FileItemProcessor.class);
	
	@Override
	public DatabaseFileImage process(String item) throws Exception {
		
		DatabaseFileImage dbImageItem = createItem(item);
		log.info("DatabaseFileImage  has been created. File Name" + item + "DBImage: " + dbImageItem);
		
		return dbImageItem;
	}
	
	private DatabaseFileImage createItem(String item) throws InterruptedException, ExecutionException{
		
		FileProcessor fileProcessor = new FileProcessor(item);
		final Future<DatabaseFileImage> dbImageTask = DataService.getFileServicePool().submit(fileProcessor);
	  		 
		DatabaseFileImage dbImageItem = dbImageTask.get();
		return dbImageItem;
			
	}

}
