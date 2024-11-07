package supernova.whokie.groupmember.controller.dto;

import lombok.Builder;
import supernova.whokie.groupmember.GroupRole;
import supernova.whokie.groupmember.service.dto.GroupMemberModel;

import java.time.LocalDate;
import java.util.List;

public class GroupMemberResponse {

    @Builder
    public record Members(
            List<Member> members
    ) {

        public static GroupMemberResponse.Members from(GroupMemberModel.Members model) {
            return Members.builder()
                    .members(
                            model.members().stream()
                                    .map(GroupMemberResponse.Member::from)
                                    .toList()
                    )
                    .build();
        }
    }

    @Builder
    public record Member(
            Long groupMemberId,
            Long userId,
            GroupRole role,
            String userName,
            String memberImageUrl,
            LocalDate joinedAt
    ) {

        public static Member from(GroupMemberModel.Member model) {
            return Member.builder()
                    .groupMemberId(model.groupMemberId())
                    .userId(model.userId())
                    .role(model.role())
                    .userName(model.userName())
                    .memberImageUrl(model.memberImageUrl())
                    .joinedAt(model.joinedAt())
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

        public static GroupMemberResponse.Option from(GroupMemberModel.Option groupMember) {
            return GroupMemberResponse.Option.builder()
                    .groupMemberId(groupMember.groupMemberId())
                    .userId(groupMember.userId())
                    .userName(groupMember.userName())
                    .imageUrl(groupMember.imageUrl())
                    .build();
        }
    }
}
