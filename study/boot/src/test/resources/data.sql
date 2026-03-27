-- 사용자
INSERT INTO users (name) VALUES
('테스트유저1'),
('테스트유저2'),
('랜덤조회유저'),
('스킵유저'),
('중복제출유저'),
('동시제출유저1'),
('동시제출유저2'),
('동시제출유저3'),
('동시제출유저4'),
('동시제출유저5'),
('스킵1회유저'),
('정답률null유저'),
('정답률집계유저100'),
('정답률집계유저101'),
('정답률집계유저102'),
('정답률집계유저103'),
('정답률집계유저104'),
('정답률집계유저105'),
('정답률집계유저106'),
('정답률집계유저107'),
('정답률집계유저108'),
('정답률집계유저109'),
('정답률집계유저110'),
('정답률집계유저111'),
('정답률집계유저112'),
('정답률집계유저113'),
('정답률집계유저114'),
('정답률집계유저115'),
('정답률집계유저116'),
('정답률집계유저117'),
('정답률집계유저118'),
('정답률집계유저119'),
('정답률집계유저120'),
('정답률집계유저121'),
('정답률집계유저122'),
('정답률집계유저123'),
('정답률집계유저124'),
('정답률집계유저125'),
('정답률집계유저126'),
('정답률집계유저127'),
('정답률집계유저128'),
('정답률집계유저129');

-- 단원
INSERT INTO chapter (name) VALUES ('1단원: 항공역학');
INSERT INTO chapter (name) VALUES ('2단원: 항공기상');

-- 문제 (1단원)
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '날개에 작용하는 4가지 힘 중 비행기를 공중에 띄우는 힘은?', '양력(Lift)은 날개 위아래의 압력 차이에 의해 발생하며 항공기를 공중에 띄우는 힘입니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '받음각(Angle of Attack)이 증가할 때 나타나는 현상으로 올바른 것을 모두 고르시오.', '받음각이 증가하면 양력이 증가하고 항력도 함께 증가합니다. 단, 임계 받음각을 초과하면 실속이 발생합니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '베르누이 원리에 대한 설명으로 올바른 것은?', '베르누이 원리에 따르면 유체의 속도가 증가하면 압력이 감소합니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '날개 위아래의 압력 차이에 의해 발생하는 힘의 이름은?', '양력(Lift)은 날개 윗면의 빠른 공기 흐름으로 인한 저압과 아랫면의 고압 차이에 의해 발생합니다.', 'SUBJECTIVE');

-- 선택지 (문제 1)
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 1, '양력(Lift)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 2, '항력(Drag)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 3, '추력(Thrust)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 4, '중력(Gravity)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 5, '원심력(Centrifugal Force)');

-- 선택지 (문제 2)
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 1, '양력이 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 2, '항력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 3, '항력이 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 4, '양력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 5, '속도가 증가한다');

-- 선택지 (문제 3)
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 1, '유체의 속도가 증가하면 압력도 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 2, '유체의 속도가 증가하면 압력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 3, '유체의 속도와 압력은 무관하다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 4, '유체의 밀도가 높을수록 속도가 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 5, '유체의 온도가 높을수록 압력이 증가한다');

-- 정답 (문제 1 - 단일)
INSERT INTO answer (problem_id, answer) VALUES (1, '1');

-- 정답 (문제 2 - 복수)
INSERT INTO answer (problem_id, answer) VALUES (2, '1');
INSERT INTO answer (problem_id, answer) VALUES (2, '3');

-- 정답 (문제 3 - 단일)
INSERT INTO answer (problem_id, answer) VALUES (3, '2');

-- 정답 (문제 4 - 주관식)
INSERT INTO answer (problem_id, answer) VALUES (4, '양력');
INSERT INTO answer (problem_id, answer) VALUES (4, 'Lift');
