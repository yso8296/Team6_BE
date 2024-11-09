package supernova.whokie.profilequestion.controller.dto;

import lombok.Builder;
import supernova.whokie.profilequestion.service.dto.ProfileQuestionModel;

import java.time.LocalDateTime;
import java.util.List;

public class ProfileQuestionResponse {

    @Builder
    public record Questions(
            List<Question> questions
    ) {

        public static Questions from(ProfileQuestionModel.InfoList infoList) {
            return Questions.builder()
                    .questions(infoList.infoList().stream().map(Question::from).toList())
                    .build();
        }
    }

    @Builder
    public record Question(
            Long profileQuestionId,
            String profileQuestionContent,
            LocalDateTime createdAt
    ) {

        public static Question from(ProfileQuestionModel.Info info) {
            return Question.builder()
                    .profileQuestionId(info.id())
                    .profileQuestionContent(info.content())
                    .createdAt(info.createdAt())
                    .build();
        }
    }
}
