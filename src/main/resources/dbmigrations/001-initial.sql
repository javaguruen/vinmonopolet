--liquibase formatted sql

--changeset javaguruen:1 dbms:all

CREATE SEQUENCE product_id_seq start with 100 increment by 1;

--rollback DROP SEQUENCE users_id_seq;

-- TABLE product
CREATE TABLE product (
  id                INT          NOT NULL DEFAULT NEXTVAL('product_id_seq'),
  datotid           TIMESTAMP,
  varenummer        VARCHAR(32),
  varenavn          VARCHAR(100),
  volum             DECIMAL(10,2),
  pris              DECIMAL(10,2),
  literpris         DECIMAL(10,2),
  varetype          VARCHAR(100),
  produktutvalg     VARCHAR(100),
  butikkategori     VARCHAR(100),
  fylde             INT,
  friskhet          INT,
  garvestoffer      INT,
  bitterhet         INT,
  sodme             VARCHAR(100),
  farge             VARCHAR(100),
  lukt              VARCHAR(512),
  smak              VARCHAR(512),
  passertil01       VARCHAR(100),
  passertil02       VARCHAR(100),
  passertil03       VARCHAR(100),
  land              VARCHAR(100),
  distrikt          VARCHAR(100),
  underdistrikt     VARCHAR(100),
  aargang           INT,
  raastoff          VARCHAR(512),
  metode            VARCHAR(1024),
  alkohol           DECIMAL(10,2),
  sukker            VARCHAR(100),
  syre              VARCHAR(100),
  lagringsgrad      VARCHAR(100),
  produsent         VARCHAR(100),
  grossist          VARCHAR(100),
  distributor       VARCHAR(100),
  emballasjetype    VARCHAR(100),
  korktype          VARCHAR(100),
  vareurl           VARCHAR(512),

  updated           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE product;

-- TABLE price
CREATE TABLE price (
  id                INT          NOT NULL DEFAULT NEXTVAL('product_id_seq'),
  datotid           TIMESTAMP,
  varenummer        VARCHAR(32),
  volum             DECIMAL(10,2),
  pris              DECIMAL(10,2),
  literpris         DECIMAL(10,2),
  produktutvalg     VARCHAR(100),
  butikkategori     VARCHAR(100),
  updated           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE price;

