package supernova.whokie.answer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import supernova.whokie.answer.controller.dto.AnswerRequest;
import supernova.whokie.answer.controller.dto.AnswerResponse;
import supernova.whokie.answer.service.AnswerService;
import supernova.whokie.answer.service.dto.AnswerModel;
import supernova.whokie.global.annotation.Authenticate;
import supernova.whokie.global.dto.GlobalResponse;
import supernova.whokie.global.dto.PagingResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
@Validated
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/common")
    public GlobalResponse common(
        @RequestBody @Valid AnswerRequest.Common request,
        @Authenticate Long userId
    ) {
        answerService.answerToCommonQuestion(userId, request.toCommand());
        return GlobalResponse.builder().message("답변 완료").build();
    }

    @PostMapping("/group")
    public GlobalResponse group(
        @RequestBody @Valid AnswerRequest.Group request,
        @Authenticate Long userId
    ) {
        answerService.answerToGroupQuestion(userId, request.toCommand());
        return GlobalResponse.builder().message("그룹 질문 답변 완료").build();
    }

    @GetMapping("/refresh")
    public AnswerResponse.Refresh refresh(
        @Authenticate Long userId
    ) {
        AnswerModel.Refresh refresh = answerService.refreshAnswerList(userId);
        return AnswerResponse.Refresh.from(refresh);
    }

    @GetMapping("/record")
    public PagingResponse<AnswerResponse.Record> getAnswerRecord(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "date", required = false) LocalDate date,
        @Authenticate Long userId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        Page<AnswerModel.Record> models = answerService.getAnswerRecord(pageable, userId,
            date);
        Page<AnswerResponse.Record> response = models.map(AnswerResponse.Record::from);
        return PagingResponse.from(response);
    }
    @GetMapping("/record/days")
    public AnswerResponse.RecordDays getAnswerRecordDays(
            @RequestParam(name = "date", defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate date,
            @Authenticate Long userId
    ){
        AnswerModel.RecordDays answerRecordDays = answerService.getAnswerRecordDays(userId, date);

        return AnswerResponse.RecordDays.from(answerRecordDays);

    }

    @GetMapping("/hint/{answer-id}")
    public AnswerResponse.Hints getHints(
        @PathVariable("answer-id") @NotNull @Min(1) Long answerId,
        @Authenticate Long userId
    ) {
        List<AnswerModel.Hint> allHints = answerService.getHints(userId, answerId);
        return AnswerResponse.Hints.from(allHints);
    }

    @PostMapping("/hint")
    public GlobalResponse purchaseHint(
        @RequestBody @Valid AnswerRequest.Purchase request,
        @Authenticate Long userId
    ) {
        answerService.purchaseHint(userId, request.toCommand());
        return GlobalResponse.builder().message("힌트를 성공적으로 구매하였습니다!").build();
    }
}
