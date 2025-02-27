package supernova.whokie.group.infrastructure.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupInfoWithMemberCount {

    Long groupId;
    String groupName;
    String description;
    String groupImageUrl;
    Long groupMemberCount;

}
