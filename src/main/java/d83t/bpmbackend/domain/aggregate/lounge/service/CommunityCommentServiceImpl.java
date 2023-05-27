package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentResponse;
import d83t.bpmbackend.domain.aggregate.lounge.entity.Community;
import d83t.bpmbackend.domain.aggregate.lounge.entity.CommunityComment;
import d83t.bpmbackend.domain.aggregate.lounge.repository.CommunityCommentRepository;
import d83t.bpmbackend.domain.aggregate.lounge.repository.CommunityRepository;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileResponse;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.profile.service.ProfileService;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommunityCommentServiceImpl implements CommunityCommentService {
    private final CommunityRepository communityRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Override
    public CommunityCommentResponse createComment(User user, Long communityId, CommunityCommentDto commentDto) {
        Community community = communityRepository.findById(communityId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY);
        });
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();

        CommunityComment communityComment = CommunityComment.builder()
                .community(community)
                .author(profile)
                .body(commentDto.getBody())
                .build();

        CommunityComment comment = communityCommentRepository.save(communityComment);

        return convertComment(user, comment);
    }

    @Override
    public CommunityCommentResponse updateComment(User user, Long communityId, Long commentId, CommunityCommentDto commentDto) {
        communityRepository.findById(communityId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY);
        });
        CommunityComment comment = communityCommentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY_COMMENT);
        });
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();

        if (!comment.getAuthor().getId().equals(profile.getId())) {
            throw new CustomException(Error.NOT_AUTHOR_OF_POST);
        }

        comment.updateBody(commentDto.getBody());

        communityCommentRepository.save(comment);

        return convertComment(user, comment);
    }

    @Override
    public void deleteComment(User user, Long communityId, Long commentId) {
        communityRepository.findById(communityId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY);
        });
        CommunityComment comment = communityCommentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY_COMMENT);
        });
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();

        if (!comment.getAuthor().getId().equals(profile.getId())) {
            throw new CustomException(Error.NOT_AUTHOR_OF_POST);
        }

        communityCommentRepository.delete(comment);

    }

    private CommunityCommentResponse convertComment(User user, CommunityComment communityComment) {
        ProfileResponse profile = profileService.getProfile(communityComment.getAuthor().getNickName());

        return CommunityCommentResponse.builder()
                .id(communityComment.getId())
                .author(CommunityCommentResponse.Author.builder()
                        .nickname(profile.getNickname())
                        .profilePath(profile.getImage()).build())
                .body(communityComment.getBody())
                .reportCount(communityComment.getReportCount())
                //TODO
                .favorited(false)
                .favoritesCount(0L)
                .createdAt(communityComment.getCreatedDate())
                .updatedAt(communityComment.getModifiedDate())
                .build();
    }
}
