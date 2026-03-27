package kr.co.teamsky.study.infra.cache;

import static org.assertj.core.api.Assertions.assertThat;

import kr.co.teamsky.study.domain.model.id.ProblemId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InMemoryCorrectRateRepositoryTest {

    private InMemoryCorrectRateRepository repository;
    private final ProblemId problemId = new ProblemId(1L);

    @BeforeEach
    void setUp() {
        repository = new InMemoryCorrectRateRepository();
    }

    @Nested
    class 정답률_계산 {

        @Test
        void 풀이_수가_30명_미만이면_null() {
            for (int i = 0; i < 29; i++) {
                repository.increment(problemId, true);
            }

            assertThat(repository.calculateCorrectRate(problemId)).isNull();
        }

        @Test
        void 풀이_수가_30명_이상이면_정답률_반환() {
            for (int i = 0; i < 30; i++) {
                repository.increment(problemId, i < 20);
            }

            assertThat(repository.calculateCorrectRate(problemId)).isEqualTo(67);
        }

        @Test
        void 전원_정답이면_100() {
            for (int i = 0; i < 30; i++) {
                repository.increment(problemId, true);
            }

            assertThat(repository.calculateCorrectRate(problemId)).isEqualTo(100);
        }

        @Test
        void 전원_오답이면_0() {
            for (int i = 0; i < 30; i++) {
                repository.increment(problemId, false);
            }

            assertThat(repository.calculateCorrectRate(problemId)).isEqualTo(0);
        }

        @Test
        void 데이터_없으면_null() {
            assertThat(repository.calculateCorrectRate(problemId)).isNull();
        }
    }

    @Nested
    class 초기_로딩 {

        @Test
        void DB에서_로딩한_값으로_정답률_계산() {
            repository.load(problemId.value(), 100, 75);

            assertThat(repository.calculateCorrectRate(problemId)).isEqualTo(75);
        }

        @Test
        void 로딩_후_increment_반영() {
            repository.load(problemId.value(), 30, 20);
            repository.increment(problemId, true);

            assertThat(repository.calculateCorrectRate(problemId)).isEqualTo(68);
        }
    }
}
