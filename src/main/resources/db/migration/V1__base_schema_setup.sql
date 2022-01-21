CREATE TABLE public.users (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(255) NULL,
	last_modified_date timestamp NULL,
	email varchar(255) NULL,
	"name" varchar(255) NULL,
	registered_date timestamp NULL,
	user_type varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.item (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(255) NULL,
	last_modified_date timestamp NULL,
	details varchar(255) NULL,
	item_type varchar(255) NULL,
	"name" varchar(255) NULL,
	price numeric(19, 2) NULL,
	quantity varchar(255) NULL,
	CONSTRAINT item_pkey PRIMARY KEY (id)
);


CREATE TABLE public.invoice (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(255) NULL,
	last_modified_date timestamp NULL,
	percentage_discount_amount numeric(19, 2) NULL,
	flat_discount_amount numeric(19, 2) NULL,
	net_amount numeric(19, 2) NULL,
	total_amount numeric(19, 2) NULL,
	user_id int8 NULL,
	CONSTRAINT invoice_pkey PRIMARY KEY (id)
);

ALTER TABLE public.invoice ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id);

INSERT INTO public.users
(id, created_by, created_date, last_modified_by, last_modified_date, email, "name", registered_date, user_type)
VALUES(1, 'Admin', '2022-01-20 16:39:35.848', 'Admin', '2022-01-20 16:39:35.848', 'testuseremployee@test.com', 'EMPLOYEE user', '2022-01-20 16:39:35.848', 'EMPLOYEE');

INSERT INTO public.users
(id, created_by, created_date, last_modified_by, last_modified_date, email, "name", registered_date, user_type)
VALUES(2, 'Admin', '2022-01-20 16:39:35.848', 'Admin', '2022-01-20 16:39:35.848', 'testuseraffiliate@test.com', 'AFFILIATE user', '2022-01-20 16:39:35.848', 'AFFILIATE');

INSERT INTO public.users
(id, created_by, created_date, last_modified_by, last_modified_date, email, "name", registered_date, user_type)
VALUES(3, 'Admin', '2018-01-20 16:39:35.848', 'Admin', '2018-01-20 16:39:35.848', 'testuser2yearother@test.com', 'OTHER two year user', '2018-01-20 16:39:35.848', 'OTHER');
INSERT INTO public.users
(id, created_by, created_date, last_modified_by, last_modified_date, email, "name", registered_date, user_type)
VALUES(4, 'Admin', '2018-01-20 16:39:35.848', 'Admin', '2018-01-20 16:39:35.848', 'testuser1year@test.com', 'OTHER one year user', '2021-01-20 16:39:35.848', 'OTHER');

insert into item (id,created_by,created_date,last_modified_by,last_modified_date,details,item_type,name,price,quantity)
values(1,'Admin',now(),'Admin',now(),'test item Shampoo','GROCERIES','Shampoo',100,50);
insert into item (id,created_by,created_date,last_modified_by,last_modified_date,details,item_type,name,price,quantity)
values(2,'Admin',now(),'Admin',now(),'test item laptop','ELECTRONICS','Laptop',200,50);
insert into item (id,created_by,created_date,last_modified_by,last_modified_date,details,item_type,name,price,quantity)
values(3,'Admin',now(),'Admin',now(),'test item Lamp','ELECTRONICS','Lamp',300,50);
insert into item (id,created_by,created_date,last_modified_by,last_modified_date,details,item_type,name,price,quantity)
values(4,'Admin',now(),'Admin',now(),'test item Oil','GROCERIES','Oil',400,0);