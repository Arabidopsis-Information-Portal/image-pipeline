INSERT INTO staging_img.md_mage
	(name,
	original_filename,
	file_extension, 
	size, 
	width, 
	height, 
	image_data,
	md5checksum)
VALUES 
	(:name, :fileName, :fileExtension, :size, :width, :height, :content, :mdCheckSum);