ALTER TABLE t_product drop column sodme;

ALTER TABLE t_product add column sodme int;

update t_product set sodme = 0;

ALTER TABLE t_product drop column sodme;

ALTER TABLE t_product add column sodme VARCHAR(100);

update t_product set sodme = '0';