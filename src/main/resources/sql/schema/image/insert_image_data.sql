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
	(:name, :filename, :extension, :size, :width, :height, :content, :md5checksum);