package supernova.whokie.question.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import supernova.whokie.group.constants.GroupConstants;
import supernova.whokie.question.service.dto.QuestionCommand;

public class QuestionRequest {

    public record CommonCreate(
            @NotNull
            String content
    ) {

        public QuestionCommand.Create toCommand() {
            return QuestionCommand.Create.builder()
                    .groupId(GroupConstants.COMMON_GROUPS_ID)
                    .content(content)
                    .build();
        }
    }

    public record GroupCreate(
            @NotNull @Min(1)
            Long groupId,
            @NotNull
            String content
    ) {

        public QuestionCommand.Create toCommand() {
            return QuestionCommand.Create.builder()
                    .groupId(groupId)
                    .content(content)
                    .build();
        }
    }

    @Builder
    public record Approve(
            @NotNull @Min(1)
            Long groupId,
            @NotNull @Min(1)
            Long questionId,
            @NotNull
            Boolean status
    ) {

        public QuestionCommand.Approve toCommand() {
            return QuestionCommand.Approve.builder()
                    .groupId(groupId)
                    .questionId(questionId)
                    .status(status)
                    .build();
        }
    }
}
