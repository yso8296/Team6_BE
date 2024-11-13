package supernova.whokie.groupmember.provider;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CodeData(
    Long groupId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime
) {

}
