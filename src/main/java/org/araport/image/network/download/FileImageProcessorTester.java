package org.araport.image.network.download;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.dataloader.DataService;
import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.utils.FileUtils;

public class FileImageProcessorTester {
	
	private static final Logger log = Logger.getLogger(FileImageProcessorTester.class);

	public static void main(String[] args) throws Exception {
		
		String[] sourceFiles = org.araport.image.utils.FileUtils.getAllFileNames(ApplicationConstants.DOWNLOAD_STAGING_DIR);
		int count = 0;
		int notNullCount = 0;
		
		for (String item: sourceFiles){
			
			log.info("Processing File: " + item);
			
			FileProcessor fileProcessor = new FileProcessor(item);
			final Future<DatabaseFileImage> dbImageTask = DataService.getFileServicePool().submit(fileProcessor);
		  		 
			DatabaseFileImage dbImage = dbImageTask.get();
		
			log.info("DatabaseFileImage  has been created. File Name" + item + "DBImage: " +dbImage);
			
			if (dbImage.getContent()!=null){
				log.info("Binary Content:" + dbImage.getContent().toString());
				notNullCount++;
			}
			count++;
			
		}
		
		log.info("Total Count Processed:" + count);
		log.info("Total Count Not Empty:" + notNullCount);

	}

}
