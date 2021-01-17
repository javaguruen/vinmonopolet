ALTER TABLE t_product
DROP COLUMN fylde;

ALTER TABLE t_product
DROP COLUMN friskhet;

ALTER TABLE t_product
DROP COLUMN garvestoffer;

ALTER TABLE t_product
DROP COLUMN bitterhet;

ALTER TABLE t_product
DROP COLUMN passertil01;

ALTER TABLE t_product
DROP COLUMN passertil02;

ALTER TABLE t_product
DROP COLUMN passertil03;

ALTER TABLE t_product
DROP COLUMN sukker;

ALTER TABLE t_product
DROP COLUMN syre;

ALTER TABLE t_product
DROP COLUMN emballasjetype;

ALTER TABLE t_product
DROP COLUMN korktype;

ALTER TABLE t_product
DROP COLUMN sodme;

ALTER TABLE t_product
DROP COLUMN lagringsgrad;

ALTER TABLE t_product
RENAME TO whisky;

ALTER TABLE t_price
RENAME TO pris;

ALTER TABLE pris
RENAME COLUMN product_id TO whisky_id;

ALTER TABLE pris
DROP COLUMN butikkategori;