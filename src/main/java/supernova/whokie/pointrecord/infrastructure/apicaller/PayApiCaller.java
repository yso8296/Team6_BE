package supernova.whokie.pointrecord.infrastructure.apicaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import supernova.whokie.global.exception.FileTypeMismatchException;
import supernova.whokie.global.exception.ForbiddenException;
import supernova.whokie.global.property.KakaoPayProperties;
import supernova.whokie.pointrecord.infrastructure.apicaller.dto.PayApproveInfoResponse;
import supernova.whokie.pointrecord.infrastructure.apicaller.dto.PayReadyInfoResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayApiCaller {

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final KakaoPayProperties kakaoPayProperties;

    public PayReadyInfoResponse payReady(int point, Long userId, String productName) {
        String url = kakaoPayProperties.readyUrl();
        Map<String, String> body = createPayReadyBody(point, userId, productName);
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            return restClient.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "SECRET_KEY "+ kakaoPayProperties.secretKey())
                    .body(jsonBody)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().is4xxClientError()) {
                            throw new ForbiddenException("카카오페이 권한이 없습니다");
                        }
                        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                            return objectMapper.readValue(response.getBody(), PayReadyInfoResponse.class);
                        }
                        throw new FileTypeMismatchException("응답받은 형식과 요청 형식이 다릅니다.");
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    public PayApproveInfoResponse payApprove(String tid, Long userId, String pgToken) {
        String url = kakaoPayProperties.approveUrl();
        Map<String, String> body = createPayApproveBody(tid, userId, pgToken);
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            return restClient.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "SECRET_KEY "+ kakaoPayProperties.secretKey())
                    .body(jsonBody)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                            return objectMapper.readValue(response.getBody(), PayApproveInfoResponse.class);
                        }
                        throw new FileTypeMismatchException("응답받은 형식과 요청 형식이 다릅니다.");
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }


    public @NotNull HashMap<String, String> createPayReadyBody(int point, Long userId, String productName) {
        var body = new HashMap<String, String>();
        body.put("cid", "TC0ONETIME");
        body.put("partner_order_id", "partner_order_id");
        body.put("partner_user_id", String.valueOf(userId));
        body.put("item_name", productName);
        body.put("quantity", String.valueOf(point));
        body.put("total_amount", String.valueOf(point * 10));
        body.put("tax_free_amount", "0");
        body.put("approval_url", kakaoPayProperties.approveRedirectUrl());
        body.put("fail_url", kakaoPayProperties.failRedirectUrl());
        body.put("cancel_url", kakaoPayProperties.cancelRedirectUrl());
        body.put("payment_method_type", "CARD"); //지불 수단 카드로 고정

        return body;
    }

    public @NotNull HashMap<String, String> createPayApproveBody(String tid, Long userId, String pgToken) {
        var body = new HashMap<String, String>();
        body.put("cid", "TC0ONETIME");
        body.put("tid", tid);
        body.put("partner_order_id", "partner_order_id");
        body.put("partner_user_id", String.valueOf(userId));
        body.put("pg_token", pgToken);

        return body;
    }



}
