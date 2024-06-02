--liquibase formatted sql

--changeset 007-filling-tables:dmitry
INSERT INTO advertisement.users (username, password, phone_number, rating, email, boosted, role) VALUES
('user123', '$2a$10$.PSEN9QPfyvpoXh9RQzdy.Wlok/5KO.iwcNYQOe.mmVgdTAeOO0AW', '+7(123)456-78-90', 0, 'storm-yes@yandex.ru', TRUE, 'ROLE_USER'),
('cool_guy', '$2a$10$pT4a.wJbqJ9S8egWxAsQDuGoW2/JtO3/sFNqKRywS1my1HrVk.riq', '+7(456)789-01-23', 100, 'john.doe@gmail.com', FALSE, 'ROLE_USER'),
('adventure_lover', '$2a$10$vUso4/3dhelewojnFMwe3eEuuYbDjhB2w8DD7whkUNI68AEmozmVO', '+7(789)012-34-56', 200, 'jane.smith@yahoo.com', FALSE, 'ROLE_USER'),
('soccer_fanatic', '$2a$10$9FTmJyd2uuYAhCs8zS29IOFu7L1A3Sgtwm7y2zk40AuAyOX7jk9YC', '+7(234)567-89-01', 200, 'alexander.wilson@hotmail.com', TRUE, 'ROLE_USER'),
('bookworm', '$2a$10$7o45UjE92My4RzkKcp8PvOamK4PcbudQV3/Yb2F0C/3tfjG.46cDK', '+7(567)890-12-34', 300, 'emily.jones@outlook.com', FALSE, 'ROLE_USER'),
('tech_guru', '$2a$10$DCVbgoez.57rY4y24LWnL.IeUcbmf.QczNAkAaHFs00Jv0tvy/2Uq', '+7(890)123-45-67', 350, 'david.brown@mail.ru', FALSE, 'ROLE_USER'),
('music_lover', '$2a$10$rc70yvIMV6qt.uvtqgXY7eNUrlm7s9t0VVnmL10ZxQkSkChk3gr9q', '+7(345)678-90-12', 400, 'sarah.wilson@icloud.com', FALSE, 'ROLE_USER'),
('travel_bug', '$2a$10$Fy0RzoBw1LWvUu.G0SAoxOlDiVoLny4JcywrHCxxZZrRZyr5sMmxK', '+7(678)901-23-45', 500, 'michael.johnson@aol.com', FALSE, 'ROLE_USER'),
('fitness_freak', '$2a$10$J.nuQTavp.Q3J3X0ZtMutef1lsuZDA.icUtzpntSfh527ZCW1I3V.', '+7(901)234-56-78', 500, 'laura.davis@yandex.ru', FALSE, 'ROLE_USER'),
('movie_buff', '$2a$10$D5GA3XIYSPLuCg3kdhskSO3NrYToLWGGJo3CWIBXSMCINDfl2c5nC', '+7(432)109-87-65', 500, 'james.miller@protonmail.com', FALSE, 'ROLE_USER'),
('gaming_pro', '$2a$10$VeQVo/2UEOlQ3BO0zv6gJuUKY/Eeq8xSXg0mpMvNvfTTHWctXeE62', '+7(210)987-65-43', 0, 'olivia.taylor@live.com', FALSE, 'ROLE_USER'),
('art_enthusiast', '$2a$10$xWKGPXDUuxxnpTI8EkAZeeKubMAyjAxWQQKz.CtNlOrvph3FKoJoW', '+7(098)765-43-21', 0, 'william.anderson@inbox.lv', FALSE, 'ROLE_USER'),
('nature_lover', '$2a$10$TRq3w57OEgUfuZLXSYCRS..9LmukEPmrRVHv9QIed.b850ky/cIJy', '+7(876)543-21-09', 0, 'sophia.thomas@bk.ru', FALSE, 'ROLE_USER'),
('foodie', '$2a$10$TjJmASFFMmKK1iVgAwZaDel8TgWEurRYL.8jTs4ECE9FPaW13TbXG', '+7(953)180-00-61', 0, 'jacob.moore@rambler.ru', FALSE, 'ROLE_USER'),
('admin', '$2a$10$/v7NnuEmQ8wvQg6oK.RFkeX1fPF25xzQIFYSz2M7BTVLkbi1RExYe', '+7(902)902-98-11', 0, 'dimaosipov00@gmail.com', FALSE, 'ROLE_ADMIN');

INSERT INTO advertisement.advertisements (user_id, price, headline, description, status) VALUES
(1, 1000, 'Smartphone', 'A portable device combining the functions of a mobile phone and a computer, typically offering internet access, touchscreen interface, and various applications.', 'ACTIVE'),
(2, 2000, 'Laptop', 'A portable computer that is small and light enough to be used on one''s lap, typically with a clamshell form factor and a built-in keyboard and display.', 'ACTIVE'),
(3, 2000, 'Headphones', 'A pair of small speakers worn over the ears to listen to audio from a connected device such as a music player, smartphone, or computer.', 'ACTIVE'),
(4, 4000, 'Backpack', 'A bag with shoulder straps that allows it to be carried on one''s back, typically used for carrying personal belongings, books, or electronic devices.', 'ACTIVE'),
(5, 5000, 'Sunglasses', 'Eyewear designed to protect the eyes from sunlight or glare, typically featuring tinted lenses and frames that cover a larger area around the eyes.', 'ACTIVE'),
(6, 5000, 'Watch', 'A small timepiece worn on the wrist or carried in a pocket, typically designed to show the time and often other information such as date, day of the week, or chronograph functions.', 'ACTIVE'),
(7, 7000, 'Sneakers', 'Casual athletic shoes with a flexible sole and typically made of canvas or leather, suitable for walking, running, or other sports activities.', 'ACTIVE'),
(8, 8000, 'Umbrella', 'A portable device consisting of a collapsible canopy supported by a central rod, used for protection against rain or sunlight.', 'ACTIVE'),
(9, 10000, 'Camera', 'A device used to capture and record still images or moving pictures, typically consisting of a lens, image sensor, and electronic components.', 'ACTIVE'),
(10, 10000, 'Perfume', 'A fragrant liquid typically made from essential oils and alcohol, applied to the skin or clothing to produce a pleasant scent.', 'ACTIVE');

INSERT INTO advertisement.comments (advertisement_id, user_id, text) VALUES
(1, 2, 'This smartphone is amazing! The camera quality is top-notch, and the battery life lasts all day.'),
(1, 3, 'I''ve been using this smartphone for a month now, and I''m impressed with its performance. It''s fast, reliable, and the screen is stunning.'),
(2, 1, 'Absolutely love this laptop! It''s sleek, powerful, and perfect for both work and entertainment. The battery life is impressive, and the display is crystal clear.'),
(3, 4, 'These headphones are amazing! The sound quality is fantastic, with deep bass and clear highs. They are also very comfortable to wear for long periods of time. Highly recommended!'),
(3, 5, 'I was disappointed with these headphones. The build quality feels cheap, and the sound is mediocre at best. They also tend to be uncomfortable after wearing them for a while. I wouldn''t recommend them.'),
(4, 6, 'I recently purchased this backpack for my hiking trips, and I must say I''m impressed. It''s incredibly spacious, with plenty of compartments to keep all my gear organized. The material feels durable, and the padded straps make it comfortable to carry even when fully loaded. Overall, I''m very satisfied with my purchase!'),
(5, 7, 'I''ve been using these sunglasses for a few weeks now, and they''re fantastic! The polarized lenses provide excellent protection from the sun''s glare, making them perfect for driving or outdoor activities. Plus, they look stylish and feel comfortable to wear all day. Highly recommend!'),
(5, 8, 'Unfortunately, I was disappointed with these sunglasses. While they look great, the lenses scratched easily, and the frame felt flimsy. Additionally, they didn''t provide much UV protection, which was a concern for me. Overall, I wouldn''t recommend them for long-term use.'),
(6, 9, 'I recently purchased this watch, and I couldn''t be happier with it! The craftsmanship is excellent, and it looks even better in person than in the pictures. The automatic movement keeps accurate time, and the design is both elegant and versatile. It''s become my everyday timepiece, and I''ve received numerous compliments on it.'),
(7, 10, 'These sneakers are a disappointment. While they initially looked stylish and felt comfortable, they started falling apart after just a few weeks of wear. The sole began to separate from the upper, and the stitching came undone. Additionally, they didn''t provide much support, and my feet would ache after wearing them for extended periods. Overall, I wouldn''t recommend these sneakers.');

INSERT INTO advertisement.messages(advertisement_id, sender_id, recipient_id, text, read) VALUES
(2, 1, 2, 'Hi! What can you tell me about Laptop?', TRUE),
(2, 2, 1, 'Good Laptop, long battery life, nice screen', TRUE),
(2, 5, 1, 'Hello, How many?', FALSE),
(3, 2, 3, 'Good afternoon. Is that a bargain?', TRUE),
(3, 3, 2, 'No', TRUE),
(4, 3, 4, 'Greetings, what color is the backpack?', TRUE),
(4, 4, 3, 'Hello! Blue', TRUE),
(5, 4, 5, 'Why are you selling sunglasses?', TRUE),
(5, 5, 4, 'Didn''t need it. Do you want it?', TRUE),
(6, 5, 6, 'What brand of watch?', TRUE),
(6, 6, 5, 'Hi, I have no idea, but something premium.', TRUE),
(7, 6, 7, 'Hi, why are you selling?', TRUE),
(7, 7, 6, 'Hi, I''ve decided to change my style', TRUE),
(8, 7, 8, 'Hi! How long have you been using the umbrella? Why didn''t you like it? Can you get a discount? Oh, and we recently took out a mortgage, give a discount for a young family))))))', TRUE),
(8, 8, 7, 'Hi, no bargaining.', FALSE),
(9, 8, 9, 'Hi, what are the features of the camera?', TRUE),
(9, 9, 8, 'Google it.', TRUE),
(10, 9, 10, 'Hi, what''s the smell of the perfume?', TRUE),
(10, 10, 9, 'Hi, it''s a nice smell with different notes', TRUE),
(1, 10, 1, 'Hi, what is the camera of the phone? Who used it? What is the reason for selling?', TRUE),
(1, 1, 10, 'Hi. The smartphone has a good camera. I''ve used it. Got bored', FALSE);

-- rollback DELETE FROM advertisement.messages;
-- rollback DELETE FROM advertisement.comments;
-- rollback DELETE FROM advertisement.advertisements;
-- rollback DELETE FROM advertisement.users;
