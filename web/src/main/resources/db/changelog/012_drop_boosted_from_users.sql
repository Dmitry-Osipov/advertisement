--liquibase formatted sql

--changeset 012-drop-boosted-from-users:dmitry
ALTER TABLE advertisement.users DROP COLUMN boosted;

--rollback ALTER TABLE advertisement.users ADD COLUMN boosted BOOLEAN DEFAULT FALSE;
