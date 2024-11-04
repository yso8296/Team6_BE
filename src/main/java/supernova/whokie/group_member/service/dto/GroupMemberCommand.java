package supernova.whokie.group_member.service.dto;

import lombok.Builder;
import supernova.whokie.global.invite_code_util.CodeData;
import supernova.whokie.global.invite_code_util.InviteCodeUtil;
import supernova.whokie.group.Groups;
import supernova.whokie.group_member.GroupMember;
import supernova.whokie.group_member.GroupRole;
import supernova.whokie.group_member.GroupStatus;
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

        public CodeData getUrlData() {
            return InviteCodeUtil.parseCodeData(inviteCode);
        }
    }

    @Builder
    public record Exit(
        Long groupId
    ) {

    }
}
