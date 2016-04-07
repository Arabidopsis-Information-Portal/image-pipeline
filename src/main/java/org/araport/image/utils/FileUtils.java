package org.araport.image.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtils {

	public static String getSqlFileContents(String fileName) {
	    StringBuffer sb = new StringBuffer();
	    try {
	        Resource resource = new ClassPathResource(fileName);
	        DataInputStream in = new DataInputStream(resource.getInputStream());
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        while ((strLine = br.readLine()) != null) {
	            sb.append(" " + strLine);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
	
	 public static String[] getAllFileNames(String filePath) throws Exception {
	        List fileNames = new ArrayList();
	        File fileDir = new File(filePath);
	        if (fileDir != null) {
	            // get list of all files
	            File[] aFiles = fileDir.listFiles();
	            if( aFiles!= null )
	            for (int x = 0; x < aFiles.length; x++) {
	                // ignore directories
	                if (aFiles[x].isDirectory())
	                    continue;
	                fileNames.add(aFiles[x].getCanonicalPath());
	            } // end for()
	        }
	        String[] ret = new String[0];
	        return (String[]) fileNames.toArray(ret);
	    }
}
