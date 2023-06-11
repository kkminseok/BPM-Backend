package d83t.bpmbackend.domain.aggregate.lounge.community.service;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.Community;
import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityFavorite;
import d83t.bpmbackend.domain.aggregate.lounge.community.repository.CommunityFavoriteRepository;
import d83t.bpmbackend.domain.aggregate.lounge.community.repository.CommunityRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityFavoriteServiceImpl implements CommunityFavoriteService {

    private final CommunityFavoriteRepository communityFavoriteRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    @Override
    public void createCommunityFavorite(Long storyId, User user) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Community community = communityRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY));

        communityFavoriteRepository.findByCommunityIdAndUserId(storyId, user.getId()).ifPresent(e -> {
            throw new CustomException(Error.ALREADY_FAVORITE);
        });

        CommunityFavorite favorite = CommunityFavorite.builder()
                .community(community)
                .user(findUser)
                .build();

        community.addCommunityFavorite(favorite);
        communityRepository.save(community);
    }

    @Override
    public void deleteCommunityFavorite(Long storyId, User user) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Community story = communityRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY));

        CommunityFavorite storyLike = communityFavoriteRepository.findByCommunityIdAndUserId(storyId, findUser.getId())
                .orElseThrow(() -> new CustomException(Error.ALREADY_UN_FAVORTIE));

        if (storyLike.getUser().getId().equals(findUser.getId())) {
            story.removeCommunityFavorite(storyLike);
            communityRepository.save(story);
        } else {
            throw new CustomException(Error.NOT_MATCH_USER);
        }
    }
}
