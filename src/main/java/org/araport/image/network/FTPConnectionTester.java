package org.araport.image.network;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPConnectionTester {

	public static void main(String[] args) throws IOException {
        FTPClient client = new FTPClient();
        client.connect("ftp.arabidopsis.org");
        client.enterLocalPassiveMode();
        client.login("anonymous", "");
        FTPFile[] files = client.listFiles("/home/tair/Images/germplasm_images/");
        for (FTPFile file : files) {
            System.out.println("File Name: "+ file.getName());
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.indexOf('.')+1);
            System.out.println("File Extension: " + fileExtension);
            
        }

}
	
}
