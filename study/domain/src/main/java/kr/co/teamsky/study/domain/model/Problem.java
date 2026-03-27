package kr.co.teamsky.study.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public record Problem(ProblemId id, ChapterId chapterId, String content, String explanation, ProblemType problemType) {
    public AnswerStatus grade(List<String> userAnswers, List<String> correctAnswers) {
        if (problemType == ProblemType.SUBJECTIVE) {
            return gradeSubjective(userAnswers, correctAnswers);
        }
        return gradeObjective(userAnswers, correctAnswers);
    }

    private AnswerStatus gradeSubjective(List<String> userAnswers, List<String> correctAnswers) {
        String userAnswer = userAnswers.getFirst().trim();
        for (String correct : correctAnswers) {
            if (correct.trim().equals(userAnswer)) {
                return AnswerStatus.CORRECT;
            }
        }
        return AnswerStatus.INCORRECT;
    }

    private AnswerStatus gradeObjective(List<String> userAnswers, List<String> correctAnswers) {
        Set<String> correctSet = new HashSet<>(correctAnswers);
        Set<String> userSet = new HashSet<>(userAnswers);

        boolean hasCorrect = userSet.stream().anyMatch(correctSet::contains);

        if (!hasCorrect) {
            return AnswerStatus.INCORRECT;
        }

        if (userSet.equals(correctSet)) {
            return AnswerStatus.CORRECT;
        }

        return AnswerStatus.PARTIAL;
    }
}
