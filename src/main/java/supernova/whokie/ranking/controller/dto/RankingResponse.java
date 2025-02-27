package supernova.whokie.ranking.controller.dto;

import java.util.List;
import lombok.Builder;
import supernova.whokie.ranking.service.dto.RankingModel;

public class RankingResponse {

    @Builder
    public record Ranks(
        List<Rank> ranks
    ) {

        public static RankingResponse.Ranks from(List<RankingModel.Rank> models) {
            return Ranks.builder()
                .ranks(models.stream().map(RankingResponse.Rank::from).toList())
                .build();
        }
    }

    @Builder
    public record Rank(
        Long rankingId,
        String question,
        int rank,
        int count,
        String groupName
    ) {

        public static RankingResponse.Rank from(RankingModel.Rank model) {
            return RankingResponse.Rank.builder()
                .rankingId(model.rankingId())
                .question(model.question())
                .rank(model.rank())
                .count(model.count())
                .groupName(model.groupName())
                .build();
        }
    }

    @Builder
    public record GroupRanks(
        List<GroupRank> ranks
    ) {

        public static RankingResponse.GroupRanks from(List<RankingModel.GroupRank> models) {
            return GroupRanks.builder()
                .ranks(models.stream().map(RankingResponse.GroupRank::from).toList())
                .build();
        }
    }

    @Builder
    public record GroupRank(
        int rank,
        int count,
        Long userId
    ) {

        public static RankingResponse.GroupRank from(RankingModel.GroupRank model) {
            return RankingResponse.GroupRank.builder()
                .rank(model.rank())
                .count(model.count())
                .userId(model.userId())
                .build();
        }
    }
}
