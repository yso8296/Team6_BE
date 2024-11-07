package supernova.whokie.ranking.service.dto;

import lombok.Builder;
import supernova.whokie.ranking.Ranking;

import java.util.List;
import java.util.Map;

public class RankingModel {

    @Builder
    public record Rank(
            Long rankingId,
            String question,
            int rank,
            int count,
            String groupName
    ) {
        public static RankingModel.Rank from(Ranking entity, int rank) {
            return RankingModel.Rank.builder()
                    .rankingId(entity.getId())
                    .question(entity.getQuestion())
                    .rank(rank)
                    .count(entity.getCount())
                    .groupName(entity.getGroups().getGroupName())
                    .build();
        }
    }

    @Builder
    public record GroupRank(
            int rank,
            int count,
            String memberName
    ) {
        public static RankingModel.GroupRank from(Map.Entry<String, Integer> entry, int rank) {
            return RankingModel.GroupRank.builder()
                    .rank(rank)
                    .count(entry.getValue())
                    .memberName(entry.getKey())
                    .build();
        }
    }

    @Builder
    public record Top3RankingEntries(
            List<Map.Entry<String, Integer>> entries
    ) {
        public static Top3RankingEntries from(Map<String, Integer> map) {
            List<Map.Entry<String, Integer>> entries = map.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3)
                    .toList();

            return Top3RankingEntries.builder()
                    .entries(entries)
                    .build();
        }
    }
}
