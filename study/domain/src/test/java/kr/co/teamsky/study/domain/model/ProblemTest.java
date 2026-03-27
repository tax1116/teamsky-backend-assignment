package kr.co.teamsky.study.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProblemTest {

    @Nested
    class 객관식_채점 {

        @Test
        void 단일_정답_맞힘() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("1"), List.of("1"));

            assertThat(status).isEqualTo(AnswerStatus.CORRECT);
        }

        @Test
        void 단일_정답_틀림() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("2"), List.of("1"));

            assertThat(status).isEqualTo(AnswerStatus.INCORRECT);
        }

        @Test
        void 복수_정답_모두_맞힘() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("1", "3"), List.of("1", "3"));

            assertThat(status).isEqualTo(AnswerStatus.CORRECT);
        }

        @Test
        void 복수_정답_하나만_맞힘_부분정답() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("1"), List.of("1", "3"));

            assertThat(status).isEqualTo(AnswerStatus.PARTIAL);
        }

        @Test
        void 오답_포함해도_정답_하나_있으면_부분정답() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("1", "5"), List.of("1", "3"));

            assertThat(status).isEqualTo(AnswerStatus.PARTIAL);
        }

        @Test
        void 정답을_하나도_포함하지_않으면_오답() {
            Problem problem = createProblem(ProblemType.OBJECTIVE);

            AnswerStatus status = problem.grade(List.of("4", "5"), List.of("1", "3"));

            assertThat(status).isEqualTo(AnswerStatus.INCORRECT);
        }
    }

    @Nested
    class 주관식_채점 {

        @Test
        void 정답_일치() {
            Problem problem = createProblem(ProblemType.SUBJECTIVE);

            AnswerStatus status = problem.grade(List.of("스택"), List.of("스택", "Stack"));

            assertThat(status).isEqualTo(AnswerStatus.CORRECT);
        }

        @Test
        void 복수_정답_중_하나_일치() {
            Problem problem = createProblem(ProblemType.SUBJECTIVE);

            AnswerStatus status = problem.grade(List.of("Stack"), List.of("스택", "Stack"));

            assertThat(status).isEqualTo(AnswerStatus.CORRECT);
        }

        @Test
        void 오답() {
            Problem problem = createProblem(ProblemType.SUBJECTIVE);

            AnswerStatus status = problem.grade(List.of("큐"), List.of("스택", "Stack"));

            assertThat(status).isEqualTo(AnswerStatus.INCORRECT);
        }

        @Test
        void 공백_트림_후_비교() {
            Problem problem = createProblem(ProblemType.SUBJECTIVE);

            AnswerStatus status = problem.grade(List.of(" 스택 "), List.of("스택"));

            assertThat(status).isEqualTo(AnswerStatus.CORRECT);
        }
    }

    private Problem createProblem(ProblemType type) {
        return new Problem(new ProblemId(1L), new ChapterId(1L), "문제", "해설", type);
    }
}
