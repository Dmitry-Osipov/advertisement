--liquibase formatted sql

--changeset 006-grant-all-privileges-to-advertisement-user:dmitry
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA advertisement TO advertisement_user;

-- rollback REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA advertisement FROM advertisement_user;
