package kr.co.teamsky.study.domain.model;

import kr.co.teamsky.study.domain.model.id.ChoiceId;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public record Choice(ChoiceId id, ProblemId problemId, int choiceNumber, String content) {}
