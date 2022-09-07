INSERT INTO member (sign_in_id, password, email, nickname, profile_image, auth_provider_type, resource_owner_id)
VALUES ('who-hoo', '1234', 'who.ho3ov@gmail.com', 'hoo', 'https://avatars.githubusercontent.com/u/68011320?v=4',
        'GITHUB', '68011320'),
       ('ak2j38', '12345678', 'ak2j38@gmail.com', 'ader', 'https://avatars.githubusercontent.com/u/29879110?v=4',
        'GITHUB', '29879110');

INSERT INTO milestone (title, description, due_date, is_closed)
VALUES ('제목만 있는 마일스톤', null, null, false),
       ('제목과 설명이 있는 마일스톤', '하지만 완료일은 없다', null, false),
       ('모든걸 다 가진 마일스톤', 'perfect', '2022-08-31', false),
       ('닫혀버린 마일스톤', 'closed', '2022-08-31', true);

INSERT INTO label (title, background_color_code, description, text_color)
VALUES ('Feature', '#d4c5f9', '기능 개발용 라벨입니다.', 'BLACK'),
       ('Docs', '#d4c510', '문서 추가용 라벨입니다.', 'WHITE'),
       ('Bugs', '#d4c505', '버그 수정용 라벨입니다.', 'BLACK'),
       ('Question', '#d4c501', '질문용 라벨입니다.', 'WHITE');

INSERT INTO issue (title, author_id, milestone_id, is_closed, created_at, last_modified_at)
VALUES ('로우앤슬로우', 1, 1, false, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('물회', 1, 1, false, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('해진뒤', 1, 1, false, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('아타리', 1, 1, false, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('오제제', 1, 2, true, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('오달', 1, 2, true, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('뱃놈', 1, 2, true, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       ('한국횟집', 1, 2, false, '2022-09-11 00:00:00', '2022-09-11 00:00:00');

INSERT INTO issue_label (issue_id, label_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 4),
       (3, 1),
       (4, 1),
       (4, 4);

INSERT INTO issue_assignee (issue_id, assignee_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 2),
       (5, 1),
       (6, 2),
       (7, 1),
       (7, 2),
       (8, 1),
       (8, 2);

INSERT INTO comment (author_id, content, issue_id, created_at, last_modified_at)
VALUES (1, '주문할 메뉴는 오리지널과 비프립플레이트입니다.', 1, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       (2, '너무 좋아요 소고기뭇국도 기대됩니다.', 1, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       (2, '물회에는 역시 오이가 들어가야죠!', 2, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       (1, '해진뒤 한 번 실패 뒤 재도전!!', 3, '2022-09-11 00:00:00', '2022-09-11 00:00:00'),
       (2, '아타리 가는날은 무슨날?', 4, '2022-09-11 00:00:00', '2022-09-11 00:00:00');

INSERT INTO reaction (comment_id, emoji, reactor_id)
VALUES (1, 'THUMBS_UP', 1),
       (1, 'THUMBS_UP', 2),
       (3, 'THUMBS_DOWN', 2),
       (2, 'LAUGH', 1),
       (4, 'PARTY_POPPER', 1),
       (3, 'CONFUSED', 2),
       (4, 'HEART', 1),
       (4, 'ROCKET', 2),
       (3, 'EYES', 2);
