package org.araport.image.reader;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component("file_reader")
public class FilePathItemReader implements ItemReader<String> {

	private static final Log log = LogFactory.getLog(FilePathItemReader.class);

	private List<String> fileList;
	private int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException {

		if (count < fileList.size()) {
            log.info("Current Count:" + count);
			return fileList.get(count++);
		} else {
			return null;
		}

	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

}
