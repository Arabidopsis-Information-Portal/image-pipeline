package org.araport.image.domain;

import java.util.Arrays;

public class DatabaseFileImage {

	private String fileName;
	private String name;
	private String fileExtension;
	private long size;
	private String mdCheckSum;
	private int width;
	private int height;
	private byte[] content;
	
	
	public DatabaseFileImage (){
		
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getMdCheckSum() {
		return mdCheckSum;
	}
	public void setMdCheckSum(String mdCheckSum) {
		this.mdCheckSum = mdCheckSum;
	}
	public int getWidth() {
		return width;
	}
	public void seWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "DatabaseFileImage [fileName=" + fileName + ", name=" + name + ", fileExtension=" + fileExtension
				+ ", size=" + size + ", mdCheckSum=" + mdCheckSum + ", weight=" + width + ", height=" + height
				;
	}
	
	
	
}
