INSERT INTO member (sign_in_id, password, email, nickname, profile_image, auth_provider_type, resource_owner_id)
VALUES ('who-hoo', '1234', 'who.ho3ov@gmail.com', 'hoo', 'https://avatars.githubusercontent.com/u/68011320?v=4', 'GITHUB', '68011320');

INSERT INTO milestone (title, description, due_date, is_closed, is_deleted)
VALUES ('제목만 있는 마일스톤', null, null, false, false),
       ('제목과 설명이 있는 마일스톤', '하지만 완료일은 없다', null, false, false),
       ('모든걸 다 가진 마일스톤', 'perfect', '2022-08-31', false, false);
