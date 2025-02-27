package supernova.whokie.user;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import supernova.whokie.user.infrastructure.repository.UserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=abcd",
    "url.secret-key=abcd"
})
@MockBean({S3Client.class, S3Template.class, S3Presigner.class, RedissonClient.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private Users user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    @DisplayName("유저 포인트 조회")
    void getUserPoint() throws Exception {
        mockMvc.perform(get("/api/user/point")
                .requestAttr("userId", String.valueOf(user.getId()))
                .requestAttr("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(100))
            .andDo(print());
    }

    private Users createUser() {
        Users user = Users.builder()
            .name("test")
            .email("test@gmail.com")
            .point(100)
            .birthDate(LocalDate.now())
            .kakaoId(1L)
            .gender(Gender.M)
            .role(Role.USER)
            .build();

        userRepository.save(user);
        return user;
    }
}
