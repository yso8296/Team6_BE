package supernova.whokie.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.profile.Profile;
import supernova.whokie.profile.ProfileVisitor;
import supernova.whokie.profile.constants.ProfileConstants;
import supernova.whokie.profile.infrastructure.repository.ProfileRepository;
import supernova.whokie.profile.infrastructure.repository.ProfileVisitCountRepository;
import supernova.whokie.profile.infrastructure.repository.ProfileVisitorRepository;
import supernova.whokie.redis.entity.RedisVisitCount;
import supernova.whokie.user.Users;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProfileWriterService {

    private final ProfileRepository profileRepository;
    private final ProfileVisitReadService profileVisitReadService;
    private final ProfileVisitorRepository profileVisitorRepository;
    private final ProfileVisitCountRepository profileVisitCountRepository;

    @Transactional
    public void saveFromKaKao(Users user) {
        Profile profile = Profile.builder()
            .users(user)
            .backgroundImageUrl(ProfileConstants.DEFAULT_PROFILE_BACKGROUND_IMAGE_URL)
            .build();
        profileRepository.save(profile);
    }

    @Transactional
    public RedisVisitCount fdsfs(Long hostId, String visitorIp) {
        var profileVisitCount = profileVisitReadService.findVisitCountBasdayId(hostId);

        if (profileVisitorRepository.existsByHostIdAndVisitorIp(hostId, visitorIp)) {
            profileVisitCount.add();
            profileVisitCountRepository.save(profileVisitCount);
        }
        profileVisitorRepository.save(ProfileVisitor.builder()
                        .visitorIp(visitorIp)
                        .hostId(hostId)
                        .visitTime(LocalDateTime.now())
                .build());

        return RedisVisitCount.builder()
                .hostId(hostId)
                .dailyVisited(profileVisitCount.getDailyVisited())
                .totalVisited(profileVisitCount.getTotalVisited())
                .build();
    }

}
