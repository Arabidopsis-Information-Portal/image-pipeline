DROP SCHEMA IF EXISTS staging_img CASCADE;
CREATE SCHEMA staging_img;


CREATE TABLE staging_img.md_mage(
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
image_data bytea not null,
md5checksum char(32),
PRIMARY KEY (image_id)
);
