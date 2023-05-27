package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentResponse;
import d83t.bpmbackend.domain.aggregate.user.entity.User;

public interface CommunityCommentService {
    CommunityCommentResponse createComment(User user, Long communityId, CommunityCommentDto commentDto);

    CommunityCommentResponse updateComment(User user, Long communityId, Long commentId, CommunityCommentDto commentDto);
}
