package org.araport.image.network.download;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.araport.image.dataloader.DataService;

public class FileContentProcessor implements Callable<FileContent>{

	private static final Logger log = Logger.getLogger(FileContentProcessor.class);
	private final FTPClient ftpClient;
	private final String filePath;
	private final String fileName;
	private final FTPFile ftpFile;
	
	public FileContentProcessor(FTPClient client, String path, String name, FTPFile file){
		ftpClient = client;
		filePath = path;
		fileName = name;
		ftpFile = file;
	}
	
	
	@Override
	public FileContent call() throws Exception {
		FileContent fileContent = new FileContent(ftpClient, filePath, fileName, ftpFile);
		
		final Future<FileContent> fileContentTask = DataService
				.getSQLTaskPooll().submit(this);
		
		fileContent = getFileContent(fileContentTask, fileContent.getPath());
		
		return fileContent;
		
	}
	
	public FileContent getFileContent(Future<FileContent> fileContentTask, String taskName){
		
		 FileContent task = null;
		 
		 while (true){
			 if (fileContentTask.isDone()){
				 log.info("Task is executed:" + "Task Id : = " + taskName);
				 break;
			 }
			 
			 if (!fileContentTask.isDone()) {
					try {
						task= fileContentTask.get();
					} catch (InterruptedException e) {
						log.error("Error executing task: " + taskName);
					} catch (ExecutionException e) {
						log.error("Error executing task: " +taskName);
						e.printStackTrace();
					}

					log.info("Waiting for task being executed. Task Name : = "
							+ taskName);
				}
		 }
		 
		return task;
		
	}

}
