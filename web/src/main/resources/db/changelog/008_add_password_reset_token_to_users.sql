--liquibase formatted sql

--changeset 008-add-password-reset-token-to-users:dmitry
ALTER TABLE advertisement.users
ADD COLUMN reset_password_token VARCHAR(255),
ADD COLUMN reset_password_token_expiry_date TIMESTAMP;

-- rollback ALTER TABLE advertisement.users
-- rollback DROP COLUMN reset_password_token,
-- rollback DROP COLUMN reset_password_token_expiry_date;
