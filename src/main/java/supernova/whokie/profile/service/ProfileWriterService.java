package supernova.whokie.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.profile.Profile;
import supernova.whokie.profile.infrastructure.repository.ProfileRepository;
import supernova.whokie.user.Users;
import supernova.whokie.user.infrastructure.apiCaller.dto.KakaoAccount;

@RequiredArgsConstructor
@Service
public class ProfileWriterService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Profile saveFromKaKao(Users user, KakaoAccount kakaoAccount) {
        Profile profile = Profile.builder()
            .users(user)
            .backgroundImageUrl(kakaoAccount.profile().profileImageUrl())
            .build();
        profileRepository.save(profile);
        return profile;
    }

}
