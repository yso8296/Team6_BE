package supernova.whokie.pointrecord.sevice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import supernova.whokie.pointrecord.constants.PointConstants;
import supernova.whokie.pointrecord.infrastructure.apicaller.PayApiCaller;
import supernova.whokie.pointrecord.infrastructure.apicaller.dto.PayApproveInfoResponse;
import supernova.whokie.pointrecord.infrastructure.apicaller.dto.PayReadyInfoResponse;
import supernova.whokie.pointrecord.sevice.dto.PointRecordModel;
import supernova.whokie.redis.service.RedisPayService;
import supernova.whokie.user.Gender;
import supernova.whokie.user.Role;
import supernova.whokie.user.Users;
import supernova.whokie.user.service.UserReaderService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointRecordServiceTest {

    @Mock
    private UserReaderService userReaderService;

    @Mock
    private PayApiCaller payApiCaller;

    @Mock
    private RedisPayService redisPayService;

    @Mock
    private PointRecordWriterService pointRecordWriterService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PointRecordService pointService;

    @Test
    @DisplayName("ReadyPurchase 테스트")
    public void testReadyPurchasePoint() {
        // given
        Long userId = 1L;
        int point = 100;
        PayReadyInfoResponse mockResponse = new PayReadyInfoResponse("test-tid", "testUrl");
        when(payApiCaller.payReady(point, userId, PointConstants.PRODUCT_NAME_POINT)).thenReturn(mockResponse);

        // when
        PointRecordModel.ReadyInfo result = pointService.readyPurchasePoint(userId, point);

        // then
        verify(redisPayService).saveTid(userId, "test-tid");
        assertThat(result).isNotNull();
        assertThat(result.nextRedirectPcUrl()).isEqualTo("testUrl");
    }

    @Test
    @DisplayName("ApprovePurchase 테스트")
    public void testApprovePurchasePoint() {
        // given
        Long userId = 1L;
        int amount = 1000;
        int point = 100;
        String pgToken = "test-pg-token";
        Users user = createUser();
        int prePoint = user.getPoint();

                PayApproveInfoResponse.Amount mockAmount = new PayApproveInfoResponse.Amount(
                amount, 0, 0, 0, 0, 0);
        PayApproveInfoResponse mockApproveResponse = new PayApproveInfoResponse(
                "test-aid", "test-tid", "test-cid", "order-123", "user-123",
                "item-name", 100, mockAmount, "CARD", "2024-11-05T10:00:00", "2024-11-05T10:05:00"
        );

        when(userReaderService.getUserById(userId)).thenReturn(user);
        when(redisPayService.getTid(userId)).thenReturn("test-tid");
        when(payApiCaller.payApprove("test-tid", userId, pgToken)).thenReturn(mockApproveResponse);

        // when
        pointService.approvePurchasePoint(userId, pgToken);

        // then
        verify(redisPayService).deleteByUserId(userId);
        assertThat(user.getPoint()).isEqualTo(prePoint + point);
    }

    public Users createUser(){
        return Users.builder()
                .name("Test User 1")
                .email("test@example.com")
                .point(100)
                .birthDate(LocalDate.now())
                .kakaoId(1234567890L)
                .gender(Gender.M)
                .imageUrl("default_image_url.jpg")
                .role(Role.USER)
                .build();
    }
}