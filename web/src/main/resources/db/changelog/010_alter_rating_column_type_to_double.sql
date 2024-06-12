--liquibase formatted sql

--changeset 010-alter-rating-column-type-to-double:dmitry
ALTER TABLE advertisement.users
    ALTER COLUMN rating TYPE DOUBLE PRECISION USING rating::double precision;

--rollback ALTER TABLE advertisement.users
--rollback ALTER COLUMN rating TYPE INTEGER USING rating::integer;
