package kr.co.teamsky.study.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CorrectRateStatsTest {

    @Test
    void 풀이_수가_30명_미만이면_null() {
        CorrectRateStats stats = new CorrectRateStats(29, 20);

        assertThat(stats.calculateCorrectRate()).isNull();
    }

    @Test
    void 풀이_수가_30명_이상이면_반올림된_정답률을_반환한다() {
        CorrectRateStats stats = new CorrectRateStats(30, 20);

        assertThat(stats.calculateCorrectRate()).isEqualTo(67);
    }

    @Test
    void 전원_정답이면_100() {
        CorrectRateStats stats = new CorrectRateStats(30, 30);

        assertThat(stats.calculateCorrectRate()).isEqualTo(100);
    }

    @Test
    void 전원_오답이면_0() {
        CorrectRateStats stats = new CorrectRateStats(30, 0);

        assertThat(stats.calculateCorrectRate()).isEqualTo(0);
    }
}
