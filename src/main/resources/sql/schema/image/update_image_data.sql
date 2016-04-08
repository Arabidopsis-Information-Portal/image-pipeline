UPDATE staging_img.md_mage m
SET
name=:name, original_filename=:filename, file_extension=:extension, size=:size, width=:width, height=:height, image_data=:content
WHERE m.md5checksum=:md5checksum;