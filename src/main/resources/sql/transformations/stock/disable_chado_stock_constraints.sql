ALTER
	TABLE chado.stock
DROP
	CONSTRAINT IF EXISTS stock_pkey CASCADE;

ALTER
	TABLE chado.stock
DROP
	CONSTRAINT IF EXISTS stock_type_id_fkey CASCADE;
ALTER
	TABLE chado.stock
DROP
	CONSTRAINT IF EXISTS stock_organism_id_fkey CASCADE;
ALTER
	TABLE chado.stock
DROP
	CONSTRAINT IF EXISTS stock_c1;
ALTER
	TABLE chado.stock
ALTER
	stock_id
DROP
	NOT NULL,
ALTER
	uniquename
DROP
	NOT NULL,
ALTER
	type_id
DROP
	NOT NULL,
ALTER
	is_obsolete
DROP
	NOT NULL;
DROP INDEX IF EXISTS stock_c1 CASCADE;
DROP INDEX IF EXISTS stock_idx1 CASCADE;
DROP INDEX IF EXISTS stock_idx2 CASCADE;
DROP INDEX IF EXISTS stock_idx3 CASCADE;
DROP INDEX IF EXISTS stock_idx4 CASCADE;
DROP INDEX IF EXISTS stock_name_ind1 CASCADE;
DROP INDEX IF EXISTS stock_pkey CASCADE;