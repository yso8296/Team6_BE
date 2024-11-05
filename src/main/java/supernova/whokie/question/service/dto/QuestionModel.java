package supernova.whokie.question.service.dto;

import lombok.Builder;
import supernova.whokie.groupmember.service.dto.GroupMemberModel;
import supernova.whokie.question.Question;
import supernova.whokie.question.QuestionStatus;
import supernova.whokie.user.service.dto.UserModel;

import java.time.LocalDate;
import java.util.List;

public class QuestionModel {

    @Builder
    public record CommonQuestion(
        Long questionId,
        String content,
        List<UserModel.PickedInfo> users
    ) {

        public static QuestionModel.CommonQuestion from(Question question, List<UserModel.PickedInfo> pickers) {
            return CommonQuestion.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .users(pickers)
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
        LocalDate createdAt
    ) {

        public static QuestionModel.Info from(Question question, QuestionStatus status) {
            return Info.builder()
                .questionId(question.getId())
                .questionContent(question.getContent())
                .groupId(question.getGroupId())
                .status(status)
                .writer(question.getWriter().getName())
                .createdAt(question.getCreatedAt().toLocalDate())
                .build();

        }
    }

    @Builder
    public record GroupQuestion(
        Long questionId,
        String content,
        List<GroupMemberModel.Option> groupMembers
    ) {

        public static QuestionModel.GroupQuestion from(
                Question question, List<GroupMemberModel.Option> groupMembers) {
            return GroupQuestion.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .groupMembers(groupMembers)
                .build();
        }
    }
}
