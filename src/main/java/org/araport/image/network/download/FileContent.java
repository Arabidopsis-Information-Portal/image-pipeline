package org.araport.image.network.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.araport.image.common.ApplicationConstants;

public class FileContent implements Callable<byte[]> {

	private final FTPClient ftpClient;
	private final String filePath;
	private final String fileName;
	private final FTPFile ftpFile;
	private static final Logger log = Logger.getLogger(FileContent.class);
	private byte[] content = new byte[1048];

	public FileContent(FTPClient client, String path, String name, FTPFile file) {

		ftpClient = client;
		filePath = path;
		fileName = name;
		ftpFile = file;

	}

	@Override
	public byte[] call() throws Exception {
		this.content = downloadFile();
		return this.content;
	}

	private long getFileSize() throws Exception {
		long fileSize = 0;

		fileSize = ftpFile.getSize();
		log.info("File: " + filePath + " ;File size: " + fileSize);

		return fileSize;
	}

	private InputStream retrieveFileStream() throws Exception {
		InputStream is = ftpClient.retrieveFileStream(filePath);
		int reply = ftpClient.getReplyCode();
		if (is == null || (!FTPReply.isPositivePreliminary(reply) && !FTPReply.isPositiveCompletion(reply))) {
			throw new Exception(ftpClient.getReplyString());
		}
		return is;
	}

	public byte[] downloadFile() throws Exception {

		byte[] buffer = new byte[1];
		Exception exception = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		boolean success = false;
		
		log.info("Downloading File has started: " + filePath);

		try {
			File downloadFile = new File(ApplicationConstants.DOWNLOAD_STAGING_DIR + "/" + fileName);
			outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
			inputStream = retrieveFileStream();

			long fileSize = getFileSize();

			buffer = new byte[(int) fileSize];
			int readCount;

			while ((readCount = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, readCount);
			}

			success = ftpClient.completePendingCommand();
			if (!success) {
				throw new Exception("File Download has not completed!");
			}
		} catch (IOException e) {
			exception = e;
			e.printStackTrace();
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
		} finally {
			if (exception != null) {
				log.error("Error occured during file download." + " File: " + filePath + "; Message: "
						+ exception.getMessage() + "; Cause:" + exception.getCause());
			} else {

				log.info("File: " + filePath + " has been downloaded successfully.");
			}

			log.info("File: " + filePath + " has been downloaded successfully.");
			if (outputStream != null) {
				outputStream.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}

		log.info("Downloading File has completed: " + filePath);
		
		return buffer;
	}

	public byte[] getContent() throws Exception {
		this.content = downloadFile();
		return this.content;
	}
	
	public String getPath(){
		return filePath;
	}

}
