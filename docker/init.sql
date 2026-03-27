SET NAMES utf8mb4;

CREATE TABLE chapter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE problem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chapter_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    explanation TEXT NOT NULL,
    problem_type VARCHAR(20) NOT NULL,
    INDEX idx_problem_chapter_id (chapter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE choice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT NOT NULL,
    choice_number INT NOT NULL,
    content VARCHAR(500) NOT NULL,
    INDEX idx_choice_problem_id (problem_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT NOT NULL,
    answer VARCHAR(500) NOT NULL,
    INDEX idx_answer_problem_id (problem_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE submission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    answer_status VARCHAR(20) NOT NULL,
    user_answers TEXT NOT NULL,
    UNIQUE INDEX uk_submission_user_problem (user_id, problem_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테스트 데이터

-- 사용자
INSERT INTO users (name) VALUES ('테스트유저1');
INSERT INTO users (name) VALUES ('테스트유저2');
INSERT INTO users (name) VALUES ('스킵1회유저');

-- 단원
INSERT INTO chapter (name) VALUES ('1단원: 항공역학');
INSERT INTO chapter (name) VALUES ('2단원: 항공기상');

-- 문제 (1단원 - 객관식 3개, 주관식 1개)
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '날개에 작용하는 4가지 힘 중 비행기를 공중에 띄우는 힘은?', '양력(Lift)은 날개 위아래의 압력 차이에 의해 발생하며 항공기를 공중에 띄우는 힘입니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '받음각(Angle of Attack)이 증가할 때 나타나는 현상으로 올바른 것을 모두 고르시오.', '받음각이 증가하면 양력이 증가하고 항력도 함께 증가합니다. 단, 임계 받음각을 초과하면 실속이 발생합니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '베르누이 원리에 대한 설명으로 올바른 것은?', '베르누이 원리에 따르면 유체의 속도가 증가하면 압력이 감소합니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (1, '날개 위아래의 압력 차이에 의해 발생하는 힘의 이름은?', '양력(Lift)은 날개 윗면의 빠른 공기 흐름으로 인한 저압과 아랫면의 고압 차이에 의해 발생합니다.', 'SUBJECTIVE');

-- 문제 (2단원 - 객관식 2개)
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (2, '적란운(Cumulonimbus)이 항공기에 미치는 위험 요소는?', '적란운은 강한 난류, 우박, 번개 등을 동반하며 항공기 운항에 매우 위험합니다.', 'OBJECTIVE');
INSERT INTO problem (chapter_id, content, explanation, problem_type) VALUES (2, '윈드시어(Wind Shear)가 가장 위험한 비행 단계는?', '윈드시어는 이착륙 시 저고도에서 가장 위험하며 급격한 양력 변화를 유발합니다.', 'OBJECTIVE');

-- 선택지 (문제 1 - 단일 정답)
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 1, '양력(Lift)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 2, '항력(Drag)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 3, '추력(Thrust)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 4, '중력(Gravity)');
INSERT INTO choice (problem_id, choice_number, content) VALUES (1, 5, '원심력(Centrifugal Force)');

-- 선택지 (문제 2 - 복수 정답)
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 1, '양력이 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 2, '항력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 3, '항력이 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 4, '양력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (2, 5, '속도가 증가한다');

-- 선택지 (문제 3 - 단일 정답)
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 1, '유체의 속도가 증가하면 압력도 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 2, '유체의 속도가 증가하면 압력이 감소한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 3, '유체의 속도와 압력은 무관하다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 4, '유체의 밀도가 높을수록 속도가 증가한다');
INSERT INTO choice (problem_id, choice_number, content) VALUES (3, 5, '유체의 온도가 높을수록 압력이 증가한다');

-- 선택지 (문제 5)
INSERT INTO choice (problem_id, choice_number, content) VALUES (5, 1, '시정 불량');
INSERT INTO choice (problem_id, choice_number, content) VALUES (5, 2, '강한 난류와 우박');
INSERT INTO choice (problem_id, choice_number, content) VALUES (5, 3, '안개 발생');
INSERT INTO choice (problem_id, choice_number, content) VALUES (5, 4, '기온 역전');
INSERT INTO choice (problem_id, choice_number, content) VALUES (5, 5, '복사 냉각');

-- 선택지 (문제 6)
INSERT INTO choice (problem_id, choice_number, content) VALUES (6, 1, '이착륙 단계');
INSERT INTO choice (problem_id, choice_number, content) VALUES (6, 2, '순항 단계');
INSERT INTO choice (problem_id, choice_number, content) VALUES (6, 3, '상승 단계');
INSERT INTO choice (problem_id, choice_number, content) VALUES (6, 4, '강하 단계');
INSERT INTO choice (problem_id, choice_number, content) VALUES (6, 5, '지상 활주 단계');

-- 정답 (문제 1 - 단일 정답: 1번)
INSERT INTO answer (problem_id, answer) VALUES (1, '1');

-- 정답 (문제 2 - 복수 정답: 1번, 3번)
INSERT INTO answer (problem_id, answer) VALUES (2, '1');
INSERT INTO answer (problem_id, answer) VALUES (2, '3');

-- 정답 (문제 3 - 단일 정답: 2번)
INSERT INTO answer (problem_id, answer) VALUES (3, '2');

-- 정답 (문제 4 - 주관식: 양력, Lift)
INSERT INTO answer (problem_id, answer) VALUES (4, '양력');
INSERT INTO answer (problem_id, answer) VALUES (4, 'Lift');

-- 정답 (문제 5 - 단일 정답: 2번)
INSERT INTO answer (problem_id, answer) VALUES (5, '2');

-- 정답 (문제 6 - 단일 정답: 1번)
INSERT INTO answer (problem_id, answer) VALUES (6, '1');
