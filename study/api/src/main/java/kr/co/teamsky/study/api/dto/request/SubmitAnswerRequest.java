package kr.co.teamsky.study.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SubmitAnswerRequest(
        @NotNull @Positive Long problemId, @NotNull @Positive Long userId, @Valid @NotNull UserAnswer userAnswer) {
    public record UserAnswer(
            @NotNull @Pattern(regexp = "OBJECTIVE|SUBJECTIVE", message = "answerType은 OBJECTIVE 또는 SUBJECTIVE여야 합니다")
                    String answerType,
            @NotEmpty List<String> answers) {}
}
