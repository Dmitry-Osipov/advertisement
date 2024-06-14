--liquibase formatted sql

--changeset 013-add-boosted-to-advertisements:dmitry
ALTER TABLE advertisement.advertisements ADD COLUMN boosted BOOLEAN DEFAULT FALSE;

--rollback ALTER TABLE advertisement.advertisements DROP COLUMN boosted;
