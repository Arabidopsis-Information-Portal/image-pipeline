package org.araport.image.network.download;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.utils.NameMatchingUtils;
import org.springframework.util.DigestUtils;

import javax.imageio.*;

public class FileProcessor implements Callable<DatabaseFileImage> {

	private static final Logger log = Logger.getLogger(FileContent.class);
	private final String fileName;

	public FileProcessor(String name) {
		fileName = name;
	}

	@Override
	public DatabaseFileImage call() throws Exception {

		BufferedImage img = null;
		Exception exception = null;
		DatabaseFileImage fileImage = new DatabaseFileImage();

		String germplasmName = NameMatchingUtils.getGermplasmNamebyFileName(fileName);
		String fileExtension = FilenameUtils.getExtension(fileName);
		File file = new File(fileName);
		
		InputStream inputStream = new FileInputStream(file);
		long size = FileUtils.sizeOf(file);
		int width = 0;
		int height = 0;
		byte[] content = new byte[1];
		String md5CheckSum = null;

		try {
			
			img = ImageIO.read(file);
			width = img.getWidth();
			height = img.getHeight();
			content = IOUtils.toByteArray(inputStream);
			md5CheckSum = DigestUtils.md5DigestAsHex(content);
			fileImage = createDatabaseImage(germplasmName, fileExtension, md5CheckSum, size, width, height, content);
			
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {

				String errorMessage = "Error occured during DatabaseImage creation" + " File: " + fileName
						+ "; Message: " + exception.getMessage() + "; Cause:" + exception.getCause();

				log.error(errorMessage);

			} else {

				log.info("Database Image has been created: " + fileImage);

			}
		}

		return fileImage;
	}

	public DatabaseFileImage createDatabaseImage(final String name, final String extension, final String md5CheckSum,
			long size, int width, int height, final byte[] fileContent) {

		DatabaseFileImage fileImage = new DatabaseFileImage();
		fileImage.setFileName(fileName);
		fileImage.setFileExtension(extension);
		fileImage.setName(name);
		fileImage.seWidth(width);
		fileImage.setHeight(height);
		fileImage.setSize(size);
		fileImage.setContent(fileContent);
		fileImage.setMdCheckSum(md5CheckSum);

		return fileImage;
	}

}
