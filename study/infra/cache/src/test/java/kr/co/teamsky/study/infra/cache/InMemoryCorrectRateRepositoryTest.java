package kr.co.teamsky.study.infra.cache;

import static org.assertj.core.api.Assertions.assertThat;

import kr.co.teamsky.study.domain.model.CorrectRateStats;
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
    class 집계_조회 {

        @Test
        void 데이터가_없으면_empty() {
            assertThat(repository.findStats(problemId)).isEmpty();
        }

        @Test
        void 풀이_수와_정답_수를_반환한다() {
            for (int i = 0; i < 30; i++) {
                repository.increment(problemId, true);
            }

            assertThat(repository.findStats(problemId)).contains(new CorrectRateStats(30, 30));
        }

        @Test
        void 정답_수는_정답인_경우에만_증가한다() {
            for (int i = 0; i < 10; i++) {
                repository.increment(problemId, i < 5);
            }

            assertThat(repository.findStats(problemId)).contains(new CorrectRateStats(10, 5));
        }
    }

    @Nested
    class 초기_로딩 {

        @Test
        void DB에서_로딩한_값으로_집계_조회() {
            repository.load(problemId.value(), 100, 75);

            assertThat(repository.findStats(problemId)).contains(new CorrectRateStats(100, 75));
        }

        @Test
        void 로딩_후_increment_반영() {
            repository.load(problemId.value(), 30, 20);
            repository.increment(problemId, true);

            assertThat(repository.findStats(problemId)).contains(new CorrectRateStats(31, 21));
        }
    }
}
