package supernova.whokie.question.service.dto;

import lombok.Builder;
import supernova.whokie.question.Question;
import supernova.whokie.question.QuestionStatus;

import java.time.LocalDateTime;

public class QuestionModel {

    @Builder
    public record CommonQuestion(
        Long questionId,
        String content
    ) {

        public static QuestionModel.CommonQuestion from(Question question) {
            return CommonQuestion.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .build();
        }

    }

    @Builder
    public record Info(
        Long questionId,
        String questionContent,
        Long groupId,
        QuestionStatus status,
        String writer,
        LocalDateTime createdAt
    ) {

        public static QuestionModel.Info from(Question question, QuestionStatus status) {
            return Info.builder()
                .questionId(question.getId())
                .questionContent(question.getContent())
                .groupId(question.getGroupId())
                .status(status)
                .writer(question.getWriter().getName())
                .createdAt(question.getCreatedAt())
                .build();

        }
    }

    @Builder
    public record GroupQuestion(
        Long questionId,
        String content
    ) {

        public static QuestionModel.GroupQuestion from(
                Question question) {
            return GroupQuestion.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .build();
        }
    }

    @Builder
    public record Admin(
            Long questionId,
            String questionContent,
            Long groupId,
            QuestionStatus status,
            LocalDateTime createdAt
    ) {
        public static QuestionModel.Admin from(Question question) {
            return QuestionModel.Admin.builder()
                    .questionId(question.getId())
                    .questionContent(question.getContent())
                    .groupId(question.getGroupId())
                    .status(question.getQuestionStatus())
                    .createdAt(question.getCreatedAt())
                    .build();
        }
    }
}
