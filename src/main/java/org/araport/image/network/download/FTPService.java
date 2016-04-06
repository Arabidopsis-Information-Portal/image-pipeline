package org.araport.image.network.download;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.araport.image.common.ApplicationConstants;

public class FTPService {
	
	private static final Log log = LogFactory
			.getLog(FTPService.class);
	
	static FTPClient client = null;
	
	private FTPService(){
			
	}

	private static class FTPServiceHolder {
		public static final FTPService INSTANCE = new FTPService();
	}

	public static FTPService getInstance() {
			return FTPServiceHolder.INSTANCE;
	}
	
	public static void initialize() throws SocketException, IOException{
		initFTPClient();
	}
	
	private static void initFTPClient() throws SocketException, IOException{
		
		log.info("Initializing FTP client...");
					
		client = new FTPClient();
        client.connect(ApplicationConstants.FTP_SERVER);
        client.enterLocalPassiveMode();
        client.login("anonymous", "");
		
	}
	
	public static FTPClient getFTPClient(){
		
		if (client==null){
			try {
				initFTPClient();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return client;
	}
	
}
