-- public.global_param definition

-- Drop table

-- DROP TABLE public.global_param;

CREATE TABLE public.global_param (
	id varchar(20) NOT NULL,
	value varchar NULL,
	"type" varchar NULL,
	fl_active bool NULL,
	CONSTRAINT global_param_pkey PRIMARY KEY (id)
);


-- public.gtw_route_history definition

-- Drop table

-- DROP TABLE public.gtw_route_history;

CREATE TABLE public.gtw_route_history (
	id uuid NOT NULL,
	id_route int4 NOT NULL,
	id_source_type varchar(1) NOT NULL,
	id_source varchar NOT NULL,
	date_created timestamp DEFAULT now() NOT NULL,
	CONSTRAINT gtw_route_history_pk PRIMARY KEY (id, date_created)
);


-- public.gtw_route_permission_role definition

-- Drop table

-- DROP TABLE public.gtw_route_permission_role;

CREATE TABLE public.gtw_route_permission_role (
	id_route int4 NOT NULL,
	id_role varchar NOT NULL,
	limit_invoke int4 NULL,
	limit_type varchar NULL,
	limit_result int4 DEFAULT 0 NOT NULL,
	limit_invoke_type varchar NULL,
	limit_record int4 DEFAULT 0 NULL,
	limit_report int4 DEFAULT 0 NULL,
	limit_user int4 DEFAULT 0 NULL,
	limit_invoke_report int4 DEFAULT 0 NULL,
	CONSTRAINT gtw_route_permission_role_pkey PRIMARY KEY (id_route, id_role)
);


-- public.gtw_route_role definition

-- Drop table

-- DROP TABLE public.gtw_route_role;

CREATE TABLE public.gtw_route_role (
	id_route int4 NOT NULL,
	id_role varchar NOT NULL,
	date_created timestamp DEFAULT now() NULL,
	CONSTRAINT gtw_route_role_pk PRIMARY KEY (id_route, id_role)
);


-- public.gtw_route_usage definition

-- Drop table

-- DROP TABLE public.gtw_route_usage;

CREATE TABLE public.gtw_route_usage (
	id bigserial NOT NULL,
	id_route int4 NOT NULL,
	id_user int4 NOT NULL,
	date_created timestamp DEFAULT now() NULL,
	CONSTRAINT gtw_route_usage_pkey PRIMARY KEY (id)
);


-- public.gtw_routes definition

-- Drop table

-- DROP TABLE public.gtw_routes;

CREATE TABLE public.gtw_routes (
	id serial4 NOT NULL,
	"path" varchar NOT NULL,
	"method" varchar NOT NULL,
	url varchar NOT NULL,
	content_type varchar NULL,
	fl_propag_status bool DEFAULT false NOT NULL,
	json_scope jsonb NULL,
	CONSTRAINT gtw_routes_pkey PRIMARY KEY (id)
);


-- public.log_gtw definition

-- Drop table

-- DROP TABLE public.log_gtw;

CREATE TABLE public.log_gtw (
	id bigserial NOT NULL,
	date_created timestamp DEFAULT now() NULL,
	log_json jsonb NULL,
	CONSTRAINT log_gtw_pkey PRIMARY KEY (id)
);


-- public.log_message definition

-- Drop table

-- DROP TABLE public.log_message;

CREATE TABLE public.log_message (
	id uuid NOT NULL,
	log_type varchar DEFAULT 'MESSAGE'::character varying NOT NULL,
	date_created timestamp DEFAULT now() NOT NULL,
	log_json jsonb NULL,
	CONSTRAINT log_message_pk PRIMARY KEY (id)
);


-- public.query_builder definition

-- Drop table

-- DROP TABLE public.query_builder;

CREATE TABLE public.query_builder (
	id serial4 NOT NULL,
	"key" varchar(10) NULL,
	value text NULL,
	"comment" varchar(50) NULL,
	has_sub bool DEFAULT false NULL,
	CONSTRAINT query_builder_key_key UNIQUE (key),
	CONSTRAINT query_builder_pkey PRIMARY KEY (id)
);


-- public.query_param definition

-- Drop table

-- DROP TABLE public.query_param;

CREATE TABLE public.query_param (
	id serial4 NOT NULL,
	"key" varchar(50) NULL,
	value text NULL,
	"comment" varchar(50) NULL,
	exclude_fields varchar NULL,
	CONSTRAINT query_param_pkey PRIMARY KEY (id),
	CONSTRAINT query_param_uk1 UNIQUE (key)
);


-- public."role" definition

-- Drop table

-- DROP TABLE public."role";

CREATE TABLE public."role" (
	id serial4 NOT NULL,
	role_name varchar NOT NULL,
	CONSTRAINT role_pk PRIMARY KEY (id)
);


-- public."user" definition

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
	id serial4 NOT NULL,
	user_name varchar NOT NULL,
	email varchar NOT NULL,
	full_name varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);


-- public.query_builder_sub definition

-- Drop table

-- DROP TABLE public.query_builder_sub;

CREATE TABLE public.query_builder_sub (
	id serial4 NOT NULL,
	id_query_builder int4 NOT NULL,
	alias_name varchar NULL,
	value text NULL,
	"comment" varchar NULL,
	CONSTRAINT query_builder_sub_pkey PRIMARY KEY (id),
	CONSTRAINT query_builder_sub_fk1 FOREIGN KEY (id_query_builder) REFERENCES public.query_builder(id)
);


-- public.task definition

-- Drop table

-- DROP TABLE public.task;

CREATE TABLE public.task (
	id serial4 NOT NULL,
	id_user int4 NOT NULL,
	description varchar NOT NULL,
	date_created timestamp NULL,
	date_started timestamp NULL,
	date_finished time NULL,
	CONSTRAINT task_pk PRIMARY KEY (id),
	CONSTRAINT task_fk1 FOREIGN KEY (id_user) REFERENCES public."user"(id)
);


-- public.user_role definition

-- Drop table

-- DROP TABLE public.user_role;

CREATE TABLE public.user_role (
	id_user int4 NOT NULL,
	id_role int4 NOT NULL,
	date_create timestamp DEFAULT now() NOT NULL,
	CONSTRAINT user_role_pk PRIMARY KEY (id_user, id_role),
	CONSTRAINT user_role_role_fk FOREIGN KEY (id_role) REFERENCES public."role"(id),
	CONSTRAINT user_role_user_fk FOREIGN KEY (id_user) REFERENCES public."user"(id)
);