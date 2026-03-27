package kr.co.teamsky.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kr.co.teamsky.study.api.dto.request.RandomProblemRequest;
import kr.co.teamsky.study.api.dto.request.SkipProblemRequest;
import kr.co.teamsky.study.api.dto.request.SubmitAnswerRequest;
import kr.co.teamsky.study.api.dto.request.SubmitAnswerRequest.UserAnswer;
import kr.co.teamsky.study.api.dto.response.ProblemHistoryResponse;
import kr.co.teamsky.study.api.dto.response.ProblemResponse;
import kr.co.teamsky.study.api.dto.response.SubmitResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProblemApiIntegrationTest {

    // userId 격리: 테스트 그룹별 다른 유저 사용
    private static final long RANDOM_USER = 3L;
    private static final long SKIP_USER = 4L;
    private static final long DUPLICATE_USER = 5L;
    private static final long SKIP_ONE_TIME_USER = 11L;
    private static final long HISTORY_RATE_NULL_USER = 12L;
    private static final long HISTORY_RATE_30_PLUS_START_USER = 13L;

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Nested
    class 랜덤_문제_조회 {

        @Test
        void 안_푼_문제가_랜덤으로_조회된다() {
            var response = restClient
                    .post()
                    .uri("/api/problems/random")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RandomProblemRequest(1L, RANDOM_USER))
                    .retrieve()
                    .body(ProblemResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.problemId()).isNotNull();
            assertThat(response.content()).isNotBlank();
            assertThat(response.problemType()).isIn("OBJECTIVE", "SUBJECTIVE");
            assertThat(response.choices()).isNotNull();
        }

        @Test
        void 존재하지_않는_단원이면_404() {
            assertThatThrownBy(() -> restClient
                            .post()
                            .uri("/api/problems/random")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new RandomProblemRequest(999L, RANDOM_USER))
                            .retrieve()
                            .body(ProblemResponse.class))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }

        @Test
        void 존재하지_않는_유저면_404() {
            assertThatThrownBy(() -> restClient
                            .post()
                            .uri("/api/problems/random")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new RandomProblemRequest(1L, 999L))
                            .retrieve()
                            .body(ProblemResponse.class))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }

        @Test
        void 모든_문제를_다_풀면_404() {
            submitAnswer(1L, RANDOM_USER, "OBJECTIVE", List.of("1"));
            submitAnswer(2L, RANDOM_USER, "OBJECTIVE", List.of("1", "3"));
            submitAnswer(3L, RANDOM_USER, "OBJECTIVE", List.of("2"));
            submitAnswer(4L, RANDOM_USER, "SUBJECTIVE", List.of("양력"));

            assertThatThrownBy(() -> restClient
                            .post()
                            .uri("/api/problems/random")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new RandomProblemRequest(1L, RANDOM_USER))
                            .retrieve()
                            .body(ProblemResponse.class))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }
    }

    @Nested
    class 문제_스킵 {

        @Test
        void 스킵하면_직전_문제가_제외된_새_문제가_조회된다() {
            var response = restClient
                    .post()
                    .uri("/api/problems/skip")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SkipProblemRequest(1L, SKIP_USER, 1L))
                    .retrieve()
                    .body(ProblemResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.problemId()).isNotEqualTo(1L);
            assertThat(response.content()).isNotBlank();
        }

        @Test
        void 스킵으로_제외된_문제는_다음_조회_1회에만_적용된다() {
            restClient
                    .post()
                    .uri("/api/problems/skip")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SkipProblemRequest(1L, SKIP_ONE_TIME_USER, 1L))
                    .retrieve()
                    .body(ProblemResponse.class);

            submitAnswer(2L, SKIP_ONE_TIME_USER, "OBJECTIVE", List.of("1", "3"));
            submitAnswer(3L, SKIP_ONE_TIME_USER, "OBJECTIVE", List.of("2"));
            submitAnswer(4L, SKIP_ONE_TIME_USER, "SUBJECTIVE", List.of("양력"));

            var response = restClient
                    .post()
                    .uri("/api/problems/random")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RandomProblemRequest(1L, SKIP_ONE_TIME_USER))
                    .retrieve()
                    .body(ProblemResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.problemId()).isEqualTo(1L);
        }

        @Test
        void 다른_챕터_문제를_스킵하면_404() {
            assertThatThrownBy(() -> restClient
                            .post()
                            .uri("/api/problems/skip")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new SkipProblemRequest(1L, SKIP_USER, 999L))
                            .retrieve()
                            .body(ProblemResponse.class))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }
    }

    @Nested
    class 문제_제출 {

        @Test
        void 객관식_정답_제출() {
            var response = submitAnswer(1L, 1L, "OBJECTIVE", List.of("1"));

            assertThat(response.problemId()).isEqualTo(1L);
            assertThat(response.answerStatus()).isEqualTo("CORRECT");
            assertThat(response.explanation()).isNotBlank();
            assertThat(response.problemAnswers()).contains("1");
        }

        @Test
        void 객관식_오답_제출() {
            var response = submitAnswer(1L, 2L, "OBJECTIVE", List.of("5"));

            assertThat(response.answerStatus()).isEqualTo("INCORRECT");
        }

        @Test
        void 복수정답_부분정답_제출() {
            var response = submitAnswer(2L, 1L, "OBJECTIVE", List.of("1", "5"));

            assertThat(response.answerStatus()).isEqualTo("PARTIAL");
        }

        @Test
        void 복수정답_모두_맞힘() {
            var response = submitAnswer(2L, 2L, "OBJECTIVE", List.of("1", "3"));

            assertThat(response.answerStatus()).isEqualTo("CORRECT");
        }

        @Test
        void 주관식_정답_제출() {
            var response = submitAnswer(4L, 1L, "SUBJECTIVE", List.of("양력"));

            assertThat(response.answerStatus()).isEqualTo("CORRECT");
        }

        @Test
        void 주관식_오답_제출() {
            var response = submitAnswer(4L, 2L, "SUBJECTIVE", List.of("항력"));

            assertThat(response.answerStatus()).isEqualTo("INCORRECT");
        }

        @Test
        void 문제_타입과_다른_answerType으로_제출하면_400() {
            assertThatThrownBy(() -> submitAnswer(1L, RANDOM_USER, "SUBJECTIVE", List.of("양력")))
                    .isInstanceOf(HttpClientErrorException.BadRequest.class);
        }

        @Test
        void 주관식에_여러_답안을_제출하면_400() {
            assertThatThrownBy(() -> submitAnswer(4L, RANDOM_USER, "SUBJECTIVE", List.of("양력", "Lift")))
                    .isInstanceOf(HttpClientErrorException.BadRequest.class);
        }

        @Test
        void 객관식에_0번을_제출하면_400() {
            assertThatThrownBy(() -> submitAnswer(1L, RANDOM_USER, "OBJECTIVE", List.of("0")))
                    .isInstanceOf(HttpClientErrorException.BadRequest.class);
        }

        @Test
        void 객관식에_범위를_벗어난_번호를_제출하면_400() {
            assertThatThrownBy(() -> submitAnswer(1L, RANDOM_USER, "OBJECTIVE", List.of("6")))
                    .isInstanceOf(HttpClientErrorException.BadRequest.class);
        }

        @Test
        void 존재하지_않는_문제_제출시_404() {
            assertThatThrownBy(() -> submitAnswer(999L, 1L, "OBJECTIVE", List.of("1")))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }

        @Test
        void 존재하지_않는_유저_제출시_404() {
            assertThatThrownBy(() -> submitAnswer(1L, 999L, "OBJECTIVE", List.of("1")))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }

        @Test
        void 이미_제출한_문제_재제출시_409() {
            submitAnswer(3L, DUPLICATE_USER, "OBJECTIVE", List.of("2"));

            assertThatThrownBy(() -> submitAnswer(3L, DUPLICATE_USER, "OBJECTIVE", List.of("1")))
                    .isInstanceOf(HttpClientErrorException.Conflict.class);
        }
    }

    @Nested
    class 풀이_이력_조회 {

        @Test
        void 제출_후_이력_조회() {
            submitAnswer(3L, 2L, "OBJECTIVE", List.of("2"));

            var response = restClient
                    .get()
                    .uri("/api/problems/history?userId=2&problemId=3")
                    .retrieve()
                    .body(ProblemHistoryResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.problemId()).isEqualTo(3L);
            assertThat(response.answerStatus()).isEqualTo("CORRECT");
            assertThat(response.userAnswers()).contains("2");
        }

        @Test
        void 정답률_집계가_30명_미만이면_null() {
            submitAnswer(5L, HISTORY_RATE_NULL_USER, "OBJECTIVE", List.of("1"));

            var response = restClient
                    .get()
                    .uri("/api/problems/history?userId=" + HISTORY_RATE_NULL_USER + "&problemId=5")
                    .retrieve()
                    .body(ProblemHistoryResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.answerCorrectRate()).isNull();
        }

        @Test
        void 정답률_집계가_30명_이상이면_반올림된_값을_반환한다() {
            for (long userId = HISTORY_RATE_30_PLUS_START_USER;
                    userId < HISTORY_RATE_30_PLUS_START_USER + 30;
                    userId++) {
                submitAnswer(
                        6L, userId, "OBJECTIVE", List.of(userId < HISTORY_RATE_30_PLUS_START_USER + 20 ? "1" : "2"));
            }

            var response = restClient
                    .get()
                    .uri("/api/problems/history?userId=" + HISTORY_RATE_30_PLUS_START_USER + "&problemId=6")
                    .retrieve()
                    .body(ProblemHistoryResponse.class);

            assertThat(response).isNotNull();
            assertThat(response.answerCorrectRate()).isEqualTo(67);
        }

        @Test
        void 풀지_않은_문제_조회시_404() {
            assertThatThrownBy(() -> restClient
                            .get()
                            .uri("/api/problems/history?userId=1&problemId=999")
                            .retrieve()
                            .body(ProblemHistoryResponse.class))
                    .isInstanceOf(HttpClientErrorException.NotFound.class);
        }

        @Test
        void 음수_쿼리파라미터면_400() {
            assertThatThrownBy(() -> restClient
                            .get()
                            .uri("/api/problems/history?userId=-1&problemId=3")
                            .retrieve()
                            .body(ProblemHistoryResponse.class))
                    .isInstanceOf(HttpClientErrorException.BadRequest.class);
        }
    }

    private SubmitResultResponse submitAnswer(Long problemId, Long userId, String answerType, List<String> answers) {
        return restClient
                .post()
                .uri("/api/problems/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SubmitAnswerRequest(problemId, userId, new UserAnswer(answerType, answers)))
                .retrieve()
                .body(SubmitResultResponse.class);
    }
}
