INSERT INTO t_product (id,datotid,varenummer,varenavn,varetype,volum,fylde,friskhet,garvestoffer,bitterhet,sodme,farge,
                       lukt,smak,
                       passertil01,passertil02,passertil03,land,distrikt,underdistrikt,aargang,raastoff,metode,alkohol,sukker,syre,lagringsgrad,
                       produsent,grossist,distributor,emballasjetype,korktype,vareurl,active,updated)
VALUES (10, '2017-05-30 00:24:43.0','12001','Bell''s Original','Whisky',0.70,0,0,0,0,'0','Lys gyllen.',
        'Aroma med preg av lyst malt, vanilje og litt røyk.','Lett og mild whisky med innslag av malt og krydder, streif av vanilje og røyk.',
        '','','','Skottland','Øvrige','Øvrige',0,'','',40.00,'0,20','Ukjent','',
        'Bell''s Dist.','Diageo Norway AS','Skanlog As','Glass','','http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-12001',1,
        '2018-09-16 19:09:57.22');

INSERT INTO t_price (id,product_id,datotid,varenummer,volum,pris,literpris,produktutvalg,butikkategori,updated,active_to,price_change)
VALUES (10,10,'2017-05-30 00:24:43.0','12001',0.70,334.90,478.43,'Bestillingsutvalget','Uavhengig sortiment', '2017-05-30 20:18:42.530',null,0.00);

INSERT INTO t_price (id,product_id,datotid,varenummer,volum,pris,literpris,produktutvalg,butikkategori,updated,active_to,price_change)
VALUES (11,10,'2019-09-30 00:24:43.0','12001',0.70,700.00,1000.00,'Bestillingsutvalget','Uavhengig sortiment', '2019-09-30 20:18:42.530',null,0.00);
