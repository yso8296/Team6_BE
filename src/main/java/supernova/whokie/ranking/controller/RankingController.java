package supernova.whokie.ranking.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import supernova.whokie.global.annotation.Authenticate;
import supernova.whokie.ranking.controller.dto.RankingResponse;
import supernova.whokie.ranking.service.RankingService;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
@Validated
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/{user-Id}")
    public RankingResponse.Ranks getProfileRanking(
            @PathVariable("user-Id") @NotNull @Min(1) Long userId
    ) {
        return RankingResponse.Ranks.from(rankingService.getUserRanking(userId));
    }

    @GetMapping("/group/{group-id}")
    public RankingResponse.GroupRanks getGroupRanking(
            @PathVariable("group-id") @NotNull @Min(1) Long groupId,
            @Authenticate Long userId
    ) {
        return RankingResponse.GroupRanks.from(rankingService.getGroupRanking(userId, groupId));
    }

}
