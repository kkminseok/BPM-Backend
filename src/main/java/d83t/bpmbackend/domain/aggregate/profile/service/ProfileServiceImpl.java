package d83t.bpmbackend.domain.aggregate.profile.service;

import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileDto;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileResponse;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileUpdateRequest;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileUpdateResponse;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.profile.repository.ProfileRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository userRepository;
    private final ProfileImageService profileImageService;
    private final ProfileRepository profileRepository;

    @Transactional
    @Override
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest profileUpdateRequest, MultipartFile file) {
        User user = userRepository.findByKakaoId(profileUpdateRequest.getKakaoId()).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_USER_ID)
        );
        Profile profile = user.getProfile();
        String storagePathName = profile.getStoragePathName();

        ProfileDto profileDto = profileImageService.updateProfileDto(storagePathName, profileUpdateRequest, file);
        Profile updateProfile = profileDto.toEntity();
        user.updateProfile(updateProfile);
        userRepository.save(user);
        return ProfileUpdateResponse.builder()
                .nickname(updateProfile.getNickName())
                .bio(updateProfile.getBio())
                .image(updateProfile.getStoragePathName())
                .build();
    }

    @Override
    public ProfileResponse getProfile(Long id) {
        log.info("Profile id : {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(()->{
            throw new CustomException(Error.NOT_FOUND_PROFILE);
        });
        return ProfileResponse.builder()
                .id(profile.getId())
                .nickname(profile.getNickName())
                .image(profile.getStoragePathName())
                .build();
    }


}
