
CREATE SCHEMA siptrunk;

SET search_path = siptrunk, pg_catalog;

CREATE TABLE trunk (
	trunk_id integer NOT NULL,
	name character varying(64)[] NOT NULL,
	created_date integer NOT NULL,
	last_updated integer NOT NULL,
	deleted boolean NOT NULL,
	deleted_date integer
);

ALTER TABLE siptrunk.trunk ALTER COLUMN trunk_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME siptrunk.trunk_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

ALTER TABLE trunk
	ADD CONSTRAINT trunk_pkey PRIMARY KEY (trunk_id);
