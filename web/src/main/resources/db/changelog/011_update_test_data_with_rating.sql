--liquibase formatted sql

--changeset 011-update-test-data-with-rating:dmitry
INSERT INTO advertisement.ratings (sender_id, recipient_id, evaluation) VALUES
(1, 4, 5),
(2, 4, 4),
(3, 9, 5),
(4, 9, 5),
(5, 9, 5),
(6, 8, 5),
(7, 10, 4),
(8, 7, 3),
(9, 7, 4),
(10, 4, 1);

UPDATE advertisement.users SET rating = 0;

UPDATE advertisement.users SET rating = 3.3333333333333335 WHERE id = 4;
UPDATE advertisement.users SET rating = 5 WHERE id = 9;
UPDATE advertisement.users SET rating = 5 WHERE id = 8;
UPDATE advertisement.users SET rating = 4 WHERE id = 10;
UPDATE advertisement.users SET rating = 3.5 WHERE id = 7;

--rollback DELETE FROM advertisement.ratings;
