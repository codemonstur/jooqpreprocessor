
SET search_path = siptrunk, pg_catalog;

ALTER TABLE trunk
	ALTER COLUMN trunk_id TYPE bigint USING trunk_id::bigint /* TYPE change - table: trunk original: integer new: bigint */,
	ALTER COLUMN created_date TYPE bigint USING created_date::bigint /* TYPE change - table: trunk original: integer new: bigint */,
	ALTER COLUMN last_updated TYPE bigint USING last_updated::bigint /* TYPE change - table: trunk original: integer new: bigint */,
	ALTER COLUMN deleted_date TYPE bigint USING deleted_date::bigint /* TYPE change - table: trunk original: integer new: bigint */,
	ALTER COLUMN name TYPE character varying(64) USING name::character varying(64) /* TYPE change - table: trunk original: character varying(64)[] new: character varying(64) */;
