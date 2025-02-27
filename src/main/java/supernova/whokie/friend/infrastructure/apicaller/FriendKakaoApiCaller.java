package supernova.whokie.friend.infrastructure.apicaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import supernova.whokie.friend.infrastructure.apicaller.dto.KakaoDto;

@Component
@RequiredArgsConstructor
public class FriendKakaoApiCaller {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoDto.Friends getKakaoFriends(String accessToken) {
        try {
            return restClient.get()
                    .uri("https://kapi.kakao.com/v1/api/talk/friends")
                    .header("Authorization", "Bearer " + accessToken)
                    .exchange((req, res) -> {
                        return objectMapper.readValue(res.getBody(), KakaoDto.Friends.class);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
