--liquibase formatted sql

--changeset 001-create-advertisement-schema:dmitry
CREATE SCHEMA IF NOT EXISTS advertisement;
ALTER SCHEMA advertisement OWNER TO advertisement_user;

-- rollback DROP SCHEMA IF EXISTS advertisement CASCADE;
