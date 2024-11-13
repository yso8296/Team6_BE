package supernova.whokie.pointrecord.controller;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import supernova.whokie.global.annotation.Authenticate;
import supernova.whokie.global.dto.GlobalResponse;
import supernova.whokie.global.dto.PagingResponse;
import supernova.whokie.pointrecord.PointRecordOption;
import supernova.whokie.pointrecord.controller.dto.PointRecordResponse;
import supernova.whokie.pointrecord.sevice.PointRecordService;
import supernova.whokie.pointrecord.sevice.dto.PointRecordCommand;
import supernova.whokie.pointrecord.sevice.dto.PointRecordModel;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
@Validated
public class PointRecordController {

    private final PointRecordService pointRecordService;

    @GetMapping("/purchase")
    public PointRecordModel.ReadyInfo purchasePoint(
        @Authenticate Long userId,
        @RequestParam("point") int point
    ) {
        return pointRecordService.readyPurchasePoint(userId, point);
    }

    @GetMapping("/purchase/approve")
    public GlobalResponse payApproved(
        @Authenticate Long userId,
        @RequestParam("pg_token") String pgToken
    ) {
        pointRecordService.approvePurchasePoint(userId, pgToken);
        return GlobalResponse.builder().message("포인트 결제가 완료되었습니다.").build();
    }

    @GetMapping("/record")
    public PagingResponse<PointRecordResponse.Record> getChargedList(
        @Authenticate Long userId,
        @RequestParam(name = "start-date", defaultValue = "1900-01-01") LocalDate startDate,
        @RequestParam(name = "end-date", defaultValue = "2100-01-01") LocalDate endDate,
        @RequestParam(name = "option") @NotNull PointRecordOption option,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PointRecordCommand.Record command = new PointRecordCommand.Record(startDate, endDate,
            option);
        Page<PointRecordResponse.Record> response = pointRecordService.getRecordsPaging(
                userId, command, pageable)
            .map(PointRecordResponse.Record::from);

        return PagingResponse.from(response);
    }
}
