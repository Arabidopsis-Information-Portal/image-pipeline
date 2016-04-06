package org.araport.image.tasklet.business;

import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.dataloader.DataService;
import org.araport.image.network.download.DownLoadStats;
import org.araport.image.network.download.FTPService;
import org.araport.image.network.download.FileContent;
import org.araport.image.staging.BatchSchemaInitTasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@PropertySources(value = { @PropertySource("classpath:/download.properties") })
public class ContentDownLoaderTasklet implements Tasklet{

	private static final Log log = LogFactory
			.getLog(ContentDownLoaderTasklet.class);
	
	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;
		

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context) throws Exception {
		
		log.info("ContentDownLoaderTasklet has started.");
		FTPService service = FTPService.getInstance();
		FTPService.initialize();
		FTPClient client = service.getFTPClient();
		
		FTPFile[] files = client.listFiles(ApplicationConstants.FTP_FOLDER);
		DownLoadStats.getInstance().TOTAL_COUNT.setValue(files.length);
		int fileCount = files.length;
		log.info("Total Count Files to Download:" + fileCount);
		
		
		for (FTPFile file : files) {
			 
			 String fileName = file.getName();
			 String filePath = ApplicationConstants.FTP_FOLDER + fileName;
						 
			 FileContent fileContent = new FileContent(client, filePath, fileName, file);
			 final Future<byte []> binaryContentTask = DataService
						.getDataServicePool().submit(fileContent);
			 
			 byte [] content =binaryContentTask.get();
		
			 log.info("Remote File has been downloaded:" + fileName + "Size: " +content.length);
			 
			 log.info(DownLoadStats.getCurrentStatistics());
			 
		 }
		 
		 log.info(DownLoadStats.getCurrentStatistics());
		 
		log.info("ContentDownLoaderTasklet has completed.");
		
		return RepeatStatus.FINISHED;
	}

}
