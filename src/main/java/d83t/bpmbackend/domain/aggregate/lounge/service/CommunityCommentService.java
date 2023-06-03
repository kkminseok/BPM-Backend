package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityCommentResponse;
import d83t.bpmbackend.domain.aggregate.user.entity.User;

import java.util.List;

public interface CommunityCommentService {
    CommunityCommentResponse createComment(User user, Long communityId, CommunityCommentDto commentDto);

    List<CommunityCommentResponse> communityGetComments(User user, Long communityId);

    CommunityCommentResponse updateComment(User user, Long communityId, Long commentId, CommunityCommentDto commentDto);

    void deleteComment(User user, Long communityId, Long commentId);

    void favoriteComment(User user, Long communityId, Long commentId);

    void unfavoriteComment(User user, Long communityId, Long commentId);

    void report(User user, Long communityId, Long commentId, ReportDto reportDto);
}
