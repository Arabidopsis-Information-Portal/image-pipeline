DROP TABLE IF EXISTS chado.md_mage CASCADE;
DROP TABLE IF EXISTS chado.md_imagedata CASCADE;
DROP TABLE IF EXISTS chado.md_imagethumbnail CASCADE;
DROP TABLE IF EXISTS chado.stock_image CASCADE;

CREATE TABLE chado.md_mage(
image_id bigserial NOT NULL,
type_id int4,
description text,
name text NOT NULL,
original_filename text,
file_extension text,
created_datetime timestamp not null default current_timestamp,
modified_datetime timestamp not null default current_timestamp,
size bigint,
width int4,
height int4,
md5checksum char(32),
PRIMARY KEY (image_id)
);

ALTER TABLE chado.md_mage
	ADD FOREIGN KEY (type_id) 
	REFERENCES chado.cvterm (cvterm_id);
	
CREATE TABLE chado.md_imagedata(
image_id bigserial not null,
image_data bytea not null,
PRIMARY KEY (image_id)
);

ALTER TABLE chado.md_imagedata
	ADD FOREIGN KEY (image_id) 
	REFERENCES chado.md_mage (image_id)  on delete cascade INITIALLY DEFERRED;
	
CREATE TABLE chado.md_imagethumbnail(
image_id bigserial not null,
width int4,
height int4,
PRIMARY KEY (image_id)
);

ALTER TABLE chado.md_imagethumbnail
	ADD FOREIGN KEY (image_id) 
	REFERENCES chado.md_mage (image_id)  on delete cascade INITIALLY DEFERRED;

CREATE TABLE chado.stock_image(
stock_image_id bigserial not null,
stock_id int4 not null,
image_id bigint not null,
PRIMARY KEY(stock_image_id)
);

ALTER TABLE chado.stock_image
	ADD FOREIGN KEY (stock_id) 
	REFERENCES chado.stock (stock_id)  on delete cascade INITIALLY DEFERRED;
	
ALTER TABLE chado.stock_image
	ADD FOREIGN KEY (image_id) 
	REFERENCES chado.md_mage (image_id)  on delete cascade INITIALLY DEFERRED;
	
ALTER TABLE chado.stock_image
	ADD CONSTRAINT stock_image_c1 UNIQUE (stock_id, image_id); 
	
CREATE INDEX stock_image_idx1 on chado.stock_image (stock_id);
CREATE INDEX stock_image_idx2 on chado.stock_image (image_id);

COMMENT ON TABLE chado.stock_image IS 'Provenance. Linking table between stocks and images';
	