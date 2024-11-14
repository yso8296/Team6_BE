package supernova.whokie.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import supernova.whokie.profile.Profile;
import supernova.whokie.profile.constants.ProfileConstants;
import supernova.whokie.profile.service.dto.ProfileCommand;
import supernova.whokie.profile.service.dto.ProfileModel;
import supernova.whokie.redis.entity.RedisVisitCount;
import supernova.whokie.redis.service.RedisVisitService;
import supernova.whokie.s3.event.S3EventDto;
import supernova.whokie.s3.service.S3Service;
import supernova.whokie.s3.util.S3Util;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileReaderService profileReaderService;
    private final RedisVisitService redisVisitService;
    private final ApplicationEventPublisher eventPublisher;
    private final S3Service s3Service;

    @Transactional
    public ProfileModel.Info getProfile(Long userId, String visitorIp) {
        Profile profile = profileReaderService.getByUserId(userId);
        String imageUrl = profile.getUsers().getImageUrl();
        if (profile.getUsers().isImageUrlStoredInS3()) {
            imageUrl = s3Service.getSignedUrl(imageUrl);
        }
        String bgImgUrl = s3Service.getSignedUrl(profile.getBackgroundImageUrl());

        RedisVisitCount visitCount = redisVisitService.visitProfile(userId, visitorIp);
        return ProfileModel.Info.from(profile, visitCount, bgImgUrl, imageUrl);
    }

    @Transactional
    public void updateImage(Long userId, MultipartFile imageFile) {
        String key = S3Util.generateS3Key(ProfileConstants.PROFILE_BG_IMAGE_FOLRDER, userId);
        S3EventDto.Upload event = S3EventDto.Upload.toDto(imageFile, key, ProfileConstants.PROFILE_BG_IMAGE_WIDTH, ProfileConstants.PROFILE_BG_IMAGE_HEIGHT);
        eventPublisher.publishEvent(event);

        Profile profile = profileReaderService.getByUserId(userId);
        profile.updateBackgroundImageUrl(key);
    }

    @Transactional
    public void modifyProfileDescription(Long userId, ProfileCommand.Modify command) {
        Profile profile = profileReaderService.getByUserId(userId);
        profile.updateDescription(command.description());
    }
}
