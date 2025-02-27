package supernova.whokie.user.infrastructure.apicaller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RefreshedTokenInfoResponse (
    String accessToken,
    String tokenType,
    Long expiresIn
) {

}
