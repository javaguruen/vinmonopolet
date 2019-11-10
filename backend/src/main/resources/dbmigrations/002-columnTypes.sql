--liquibase formatted sql

--changeset javaguruen:3 dbms:all

--ALTER TABLE t_product ALTER COLUMN sodme TYPE INT USING sodme::integer;
ALTER TABLE t_product drop column sodme;

ALTER TABLE t_product add column sodme int;

update t_product set sodme = 0;

--rollback
ALTER TABLE t_product drop column sodme;

ALTER TABLE t_product add column sodme VARCHAR(100);

update t_product set sodme = '0';


