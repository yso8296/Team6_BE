package supernova.whokie.question.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import supernova.whokie.global.annotation.Authenticate;
import supernova.whokie.global.dto.GlobalResponse;
import supernova.whokie.global.dto.PagingResponse;
import supernova.whokie.question.QuestionStatus;
import supernova.whokie.question.controller.dto.QuestionRequest;
import supernova.whokie.question.controller.dto.QuestionResponse;
import supernova.whokie.question.service.QuestionService;
import supernova.whokie.question.service.dto.QuestionModel;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/group/{group-id}/question/random")
    public QuestionResponse.GroupQuestions getGroupQuestionList(
        @PageableDefault(page = 0, size = 5) Pageable pageable,
        @PathVariable("group-id") @NotNull @Min(1) Long groupId,
        @Authenticate Long userId
    ) {
        List<QuestionModel.GroupQuestion> groupQuestions = questionService.getGroupQuestions(userId, groupId, pageable);
        return QuestionResponse.GroupQuestions.from(groupQuestions);
    }

    @PostMapping("/group/question")
    public GlobalResponse createGroupQuestion(
        @RequestBody @Valid QuestionRequest.GroupCreate request,
        @Authenticate Long userId
    ) {
        questionService.createGroupQuestion(userId, request.toCommand());
        return GlobalResponse.builder().message("질문이 성공적으로 생성되었습니다.").build();
    }

    @PatchMapping("/group/question/status")
    public GlobalResponse approveGroupQuestion(
        @RequestBody @Valid QuestionRequest.Approve request,
        @Authenticate Long userId
    ) {
        questionService.approveQuestion(userId, request.toCommand());
        return GlobalResponse.builder().message("그룹 질문 승인에 성공하였습니다.").build();
    }

    @GetMapping("/group/{group-id}/question")
    public PagingResponse<QuestionResponse.Info> getGroupQuestionPaging(
        @Authenticate Long userId,
        @PathVariable("group-id") @NotNull @Min(1) Long groupId,
        @RequestParam("status") @NotNull QuestionStatus status,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<QuestionModel.Info> groupQuestionInfoList = questionService.getGroupQuestionPaging(
            userId, groupId, status, pageable);
        QuestionResponse.Infos result = QuestionResponse.Infos.from(groupQuestionInfoList);
        return PagingResponse.from(result.infos());
    }

    @GetMapping("/common/question/random")
    public QuestionResponse.CommonQuestions getCommonQuestions(
        @Authenticate Long userId,
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        List<QuestionModel.CommonQuestion> commonQuestions = questionService.getCommonQuestion(pageable);
        return QuestionResponse.CommonQuestions.from(commonQuestions);
    }

}
