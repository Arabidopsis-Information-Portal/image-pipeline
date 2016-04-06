package org.araport.image.network.download;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Future;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.dataloader.DataService;

public class ContentDownloader {
	
	private static final Log log = LogFactory
			.getLog(ContentDownloader.class);
	
		
	  //     logger.addHandler(handler);

	
	public static void main(String[] args) throws Exception {

		
		FTPService service = FTPService.getInstance();
		FTPService.initialize();
		FTPClient client = service.getFTPClient();
		
		FTPFile[] files = client.listFiles(ApplicationConstants.FTP_FOLDER);
		 for (FTPFile file : files) {
	            System.out.println("File Name: "+ file.getName());
	            String fileName = file.getName();
	            String fileExtension = fileName.substring(fileName.indexOf('.')+1);
	            System.out.println("File Extension: " + fileExtension);
	            
	            
	            
	        }
		 
		 /* TEST WORKING CODE
		 String fileName = files[0].getName();
		 FTPFile file = files[0];
		 String filePath = ApplicationConstants.FTP_FOLDER + fileName;
		 
		 log.info("File Path:" + filePath);
		 FileContent fileContent = new FileContent(client, filePath, fileName, file);
		 byte[] byteContent = fileContent.downloadFile();
		 log.info("File Content:" + byteContent);
		 log.info("File Content Length:" + byteContent.length);
		 **/
		 
		 
		 DownLoadStats.getInstance().TOTAL_COUNT.setValue(files.length);
		 
		 for (FTPFile file : files) {
			 
			 String fileName = file.getName();
			 String filePath = ApplicationConstants.FTP_FOLDER + fileName;
			 //FileContentProcessor fileProcessor = new FileContentProcessor(client,fileName);
			 
			 //final Future<FileContent> fileContentTask = DataService
				//		.getDataServicePool().submit(fileProcessor);
			 
			 FileContent fileContent = new FileContent(client, filePath, fileName, file);
			 final Future<byte []> binaryContentTask = DataService
						.getDataServicePool().submit(fileContent);
			 
			 //FileContent content = fileProcessor.getFileContent(fileContentTask, fileName);
			 
			 byte [] content =binaryContentTask.get();
			// byte [] binaryContent = content.getContent();
			 log.info("Remote File has been downloaded:" + fileName + "Size: " +content.length);
			 
			 log.info(DownLoadStats.getCurrentStatistics());
			 
		 }
		 
		 log.info(DownLoadStats.getCurrentStatistics());
		 
	}

}
