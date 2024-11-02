package supernova.whokie.user.service;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import supernova.whokie.global.entity.BaseTimeEntity;
import supernova.whokie.user.Gender;
import supernova.whokie.user.Role;
import supernova.whokie.user.Users;
import supernova.whokie.user.service.dto.UserModel;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockBean({S3Client.class, S3Template.class, S3Presigner.class})
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    UserReaderService userReaderService;

    private Users user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    @DisplayName("내 포인트 조회")
    void getPoint() {
        // given
        given(userReaderService.getUserById(user.getId())).willReturn(user);

        // when
        UserModel.Point point = userService.getPoint(user.getId());

        // then
        assertAll(
            () -> assertEquals(1000, point.amount()),
            () -> then(userReaderService).should().getUserById(user.getId())
        );
    }

    private Users createUser() {
        return user = Users.builder()
            .id(1L)
            .name("test")
            .email("test@gmail.com")
            .point(1000)
            .age(22)
            .kakaoId(1L)
            .gender(Gender.M)
            .role(Role.USER)
            .build();
    }
}
