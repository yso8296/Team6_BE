package supernova.whokie.question;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import supernova.whokie.friend.Friend;
import supernova.whokie.friend.infrastructure.repository.FriendRepository;
import supernova.whokie.group.Groups;
import supernova.whokie.group.infrastructure.repository.GroupRepository;
import supernova.whokie.groupmember.GroupMember;
import supernova.whokie.groupmember.GroupRole;
import supernova.whokie.groupmember.GroupStatus;
import supernova.whokie.groupmember.infrastructure.repository.GroupMemberRepository;
import supernova.whokie.question.infrastructure.repository.QuestionRepository;
import supernova.whokie.user.Gender;
import supernova.whokie.user.Role;
import supernova.whokie.user.Users;
import supernova.whokie.user.infrastructure.repository.UserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=abcd",
        "url.secret-key=abcd",
        "spring.sql.init.mode=never"
})
@MockBean({S3Client.class, S3Template.class, S3Presigner.class, RedissonClient.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QuestionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        Users user = createUser(1);

        for (int i = 1; i <= 5; i++) {
            Users friendUser = createFriendUser(i);

            setFriendRelation(user, friendUser);
        }

        Groups group = createGroup(1);

        createGroupMember(user, group);

        //승인된 그룹질문
        for (int i = 1; i <= 10; i++) {
            createQuestion(i, user, QuestionStatus.APPROVED);
        }
        //거절된 그룹질문
        for (int i = 11; i <= 20; i++) {
            createQuestion(i, user, QuestionStatus.REJECTED);
        }
        for (int i = 7; i <= 16; i++) {
            createGroupMemberByGroupRole(i, group, GroupRole.MEMBER);
        }

        createGroupMemberByGroupRole(17, group, GroupRole.LEADER);

    }


    @Test
    @DisplayName("질문과 친구 목록을 정상적으로 가져오는지 테스트")
    void getCommonQuestionTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", "1");

        mockMvc.perform(get("/api/common/question/random")
                .requestAttr("userId", "1")
                .requestAttr("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questions").isArray())
            .andExpect(jsonPath("$.questions.length()").value(5));
    }

    @Test
    @DisplayName("랜덤 그룹 질문 조회 테스트")
    void getGroupQuestionTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", "7");

        mockMvc.perform(get("/api/group/{group-id}/question/random", 1L)
                .requestAttr("userId", "7")
                .requestAttr("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questions").isArray())
            .andExpect(jsonPath("$.questions.length()").value(5));
    }

    @Test
    @DisplayName("그룹 질문 생성 테스트")
    void createGroupQuestion() throws Exception {
        String requestJson = """
                {
                    "groupId": 1,
                    "content": "Test question"
                }
            """;

        mockMvc.perform(post("/api/group/question")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .requestAttr("userId", "7")
                .requestAttr("role", "USER"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("질문이 성공적으로 생성되었습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("그룹 질문 승인 테스트")
    void approveGroupQuestion() throws Exception {
        String requestJson = """
                {
                    "groupId": 1,
                    "questionId": 1,
                    "status" : true
                }
            """;

        mockMvc.perform(patch("/api/group/question/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .requestAttr("userId", String.valueOf(17))
                .requestAttr("role", "USER"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("그룹 질문 승인에 성공하였습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("상태에 따른 질문 목록을 정상적으로 가져오는지 테스트 (APPROVED 상태)")
    void getGroupQuestionPagingApprovedTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", "1");

        mockMvc.perform(get("/api/group/1/question")
                .param("status", "APPROVED") // APPROVED 상태
                .requestAttr("userId", "1")
                .requestAttr("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalElements").value(10))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.page").value(0));
    }

    @Test
    @DisplayName("상태에 따른 질문 목록을 정상적으로 가져오는지 테스트 (REJECTED 상태)")
    void getGroupQuestionPagingRejectedTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("userId", "1");

        mockMvc.perform(get("/api/group/1/question")
                .param("status", "REJECTED") // REJECTED 상태
                .requestAttr("userId", "1")
                .requestAttr("role", "USER")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalElements").value(10))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.page").value(0));
    }

    private void createGroupMemberByGroupRole(int index, Groups group, GroupRole leader) {
        groupMemberRepository.save(GroupMember.builder()
            .user(userRepository.save(
                Users.builder()
                    .name("Test User")
                    .email("test" + index + "@example.com")
                    .point(0)
                    .birthDate(LocalDate.now())
                    .kakaoId(1234567890L)
                    .gender(Gender.M)
                    .imageUrl("default_image_url.jpg")
                    .role(Role.USER)
                    .build()
            ))
            .group(group)
            .groupRole(leader)
            .groupStatus(GroupStatus.APPROVED)
            .build());
    }

    private void createQuestion(int index, Users user, QuestionStatus approved) {
        Question question = Question.builder()
            .id((long) index)
            .content("Question " + index)
            .writer(user)
            .groupId(1L)
            .questionStatus(approved)
            .build();
        questionRepository.save(question);
    }

    private Groups createGroup(int index) {
        Groups group = Groups.builder()
            .groupName("test group " + index)
            .groupImageUrl("test imageUrl")
            .description("test group desc")
            .build();
        groupRepository.save(group);
        return group;
    }

    private void setFriendRelation(Users user, Users friendUser) {
        Friend friend = Friend.builder()
            .hostUser(user)
            .friendUser(friendUser)
            .build();
        friendRepository.save(friend);
    }

    private Users createFriendUser(int index) {
        Users friendUser = Users.builder()
            .name("Friend " + index)
            .email("friend" + index + "@example.com")
            .point(0)
            .birthDate(LocalDate.now())
            .kakaoId(1234567890L + index)
            .gender(Gender.F)
            .imageUrl("default_image_url_friend_" + index + ".jpg")
            .role(Role.USER)
            .build();
        userRepository.save(friendUser);
        return friendUser;
    }

    private Users createUser(int index) {
        Users user = Users.builder()
            .name("Test User " + index)
            .email("test@example.com")
            .point(0)
            .birthDate(LocalDate.now())
            .kakaoId(1234567890L)
            .gender(Gender.M)
            .imageUrl("default_image_url.jpg")
            .role(Role.USER)
            .build();
        userRepository.save(user);
        return user;
    }

    private void createGroupMember(Users user, Groups group) {
        groupMemberRepository.save(GroupMember.builder()
            .user(user)
            .group(group)
            .groupStatus(GroupStatus.APPROVED)
            .groupRole(GroupRole.LEADER)
            .build());
    }


}