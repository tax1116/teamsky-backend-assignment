package kr.co.teamsky.study.domain.model;

public record CorrectRateStats(int solveCount, int correctCount) {

    private static final int MINIMUM_SOLVE_COUNT = 30;

    public Integer calculateCorrectRate() {
        if (solveCount < MINIMUM_SOLVE_COUNT) {
            return null;
        }
        return Math.round((float) correctCount / solveCount * 100);
    }
}
