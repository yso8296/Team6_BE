package supernova.whokie.groupmember.service.dto;

import lombok.Builder;
import supernova.whokie.groupmember.GroupMember;
import supernova.whokie.groupmember.GroupRole;

import java.time.LocalDate;
import java.util.List;

public class GroupMemberModel {

    @Builder
    public record Member(
            Long groupMemberId,
            Long userId,
            String userName,
            String memberImageUrl,
            LocalDate joinedAt,
            GroupRole role
    ) {

        public static Member from(GroupMember groupMember, String imageUrl) {
            return Member.builder()
                    .groupMemberId(groupMember.getId())
                    .userId(groupMember.getUser().getId())
                    .userName(groupMember.getUser().getName())
                    .memberImageUrl(imageUrl)
                    .joinedAt(groupMember.getCreatedAt().toLocalDate())
                    .role(groupMember.getGroupRole())
                    .build();
        }
    }

    @Builder
    public record Members(
            List<Member> members
    ) {

        public static Members from(List<Member> members) {
            return Members.builder()
                    .members(members)
                    .build();
        }
    }

    @Builder
    public record Option(
            Long groupMemberId,
            Long userId,
            String userName,
            String imageUrl
    ) {

        public static Option from(GroupMember groupMember, String imageUrl) {
            return Option.builder()
                    .groupMemberId(groupMember.getId())
                    .userId(groupMember.getUser().getId())
                    .userName(groupMember.getUser().getName())
                    .imageUrl(imageUrl)
                    .build();
        }
    }
}
