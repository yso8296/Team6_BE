package supernova.whokie.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.global.constants.MessageConstants;
import supernova.whokie.global.exception.EntityNotFoundException;
import supernova.whokie.group.Groups;
import supernova.whokie.group.infrastructure.repository.GroupRepository;
import supernova.whokie.group.infrastructure.repository.dto.GroupInfoWithMemberCount;

@Service
@RequiredArgsConstructor
public class GroupReaderService {

    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public Groups getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException(
            MessageConstants.GROUP_NOT_FOUND_MESSAGE));
    }

    @Transactional(readOnly = true)
    public Page<GroupInfoWithMemberCount> getGroupPaging(Long userId, Pageable pageable) {
        return groupRepository.findGroupsWithMemberCountByUserId(
            userId, pageable);
    }

    @Transactional(readOnly = true)
    public GroupInfoWithMemberCount getGroupInfoWithMemberCountByGroupId(Long groupId) {
        return groupRepository.findGroupInfoWithMemberCountByGroupId(groupId)
            .orElseThrow(
                () -> new EntityNotFoundException(MessageConstants.GROUP_NOT_FOUND_MESSAGE));
    }

    @Transactional(readOnly = true)
    public boolean isGroupExist(Long groupId) {
        return groupRepository.existsById(groupId);
    }

    @Transactional(readOnly = true)
    public Page<Groups> getALlGroupPaging(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Groups> getGroupsByName(Pageable pageable, String groupName) {
        return groupRepository.findAllByGroupNameContaining(groupName, pageable);
    }
}
