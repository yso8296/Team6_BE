package supernova.whokie.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.global.auth.JwtProvider;
import supernova.whokie.profile.service.ProfileVisitWriterService;
import supernova.whokie.profile.service.ProfileWriterService;
import supernova.whokie.redis.service.KakaoTokenService;
import supernova.whokie.user.Users;
import supernova.whokie.user.infrastructure.apicaller.UserApiCaller;
import supernova.whokie.user.infrastructure.apicaller.dto.KakaoAccount;
import supernova.whokie.user.infrastructure.apicaller.dto.TokenInfoResponse;
import supernova.whokie.user.infrastructure.apicaller.dto.UserInfoResponse;
import supernova.whokie.user.infrastructure.repository.UserRepository;
import supernova.whokie.user.service.dto.UserModel;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProfileWriterService profileWriterService;
    private final ProfileVisitWriterService profileVisitWriterService;
    private final JwtProvider jwtProvider;
    private final UserApiCaller userApiCaller;
    private final UserWriterService userWriterService;
    private final UserReaderService userReaderService;
    private final KakaoTokenService kakaoTokenService;

    public String getCodeUrl() {
        return userApiCaller.createCodeUrl();
    }

    //TODO 리팩 필요
    @Transactional
    public UserModel.Login register(String code) {
        // 토큰 발급
        TokenInfoResponse tokenResponse = userApiCaller.getAccessToken(code);
        String accessToken = tokenResponse.accessToken();
        // 카카오 사용자 정보 요청
        UserInfoResponse userInfoResponse = userApiCaller.extractUserInfo(accessToken);
        KakaoAccount kakaoAccount = userInfoResponse.kakaoAccount();

        // Users 저장 및 중복 체크
        Users user = userReaderService.findByEmail(kakaoAccount.email())
            .orElseGet(() -> {
                Users newUser = userWriterService.saveUserFromKakao(userInfoResponse);
                profileWriterService.saveFromKaKao(newUser, kakaoAccount);
                profileVisitWriterService.save(newUser.getId());
                return newUser;
            });

        // kakao token 저장
        kakaoTokenService.saveToken(user.getId(), tokenResponse);
        String jwt = jwtProvider.createToken(user.getId(), user.getRole());
        return UserModel.Login.from(jwt, user.getId());
    }

    public UserModel.Info getUserInfo(Long userId) {
        Users user = userReaderService.getUserById(userId);
        return UserModel.Info.from(user);
    }

    public UserModel.Point getPoint(Long userId) {
        Users user = userReaderService.getUserById(userId);
        return UserModel.Point.from(user);
    }
}
