package supernova.whokie.groupmember.service.dto;

import lombok.Builder;
import supernova.whokie.group.Groups;
import supernova.whokie.groupmember.GroupMember;
import supernova.whokie.groupmember.GroupRole;
import supernova.whokie.groupmember.GroupStatus;
import supernova.whokie.groupmember.provider.CodeData;
import supernova.whokie.groupmember.provider.InviteCodeProvider;
import supernova.whokie.user.Users;

public class GroupMemberCommand {

    @Builder
    public record Modify(
        Long groupId,
        Long pastLeaderId,
        Long newLeaderId
    ) {

    }

    @Builder
    public record Expel(
        Long groupId,
        Long userId
    ) {

    }

    @Builder
    public record Join(
        String inviteCode
    ) {

        public GroupMember toEntity(Users user, Groups group) {
            return GroupMember.builder()
                .user(user)
                .group(group)
                .groupRole(GroupRole.MEMBER)
                .groupStatus(GroupStatus.APPROVED)
                .build();
        }

        public CodeData getUrlData(InviteCodeProvider inviteCodeProvider) {
            return inviteCodeProvider.parseCodeData(inviteCode);
        }
    }

    @Builder
    public record Exit(
        Long groupId
    ) {

    }
}
