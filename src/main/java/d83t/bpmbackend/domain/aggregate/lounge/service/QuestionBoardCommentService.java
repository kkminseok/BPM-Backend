package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentResponse;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentUpdateDto;
import d83t.bpmbackend.domain.aggregate.user.entity.User;

import java.util.List;

public interface QuestionBoardCommentService {

    QuestionBoardCommentResponse createComment(User user, Long questionBoardArticleId, QuestionBoardCommentDto commentDto);

    List<QuestionBoardCommentResponse> getComments(User user, Long questionBoardArticleId);

    QuestionBoardCommentResponse updateComment(User user, Long questionBoardArticleId, Long commentId, QuestionBoardCommentUpdateDto questionBoardCommentUpdateDto);

    void deleteComment(User user, Long questionBoardArticleId, Long commentId);

    QuestionBoardCommentResponse reportComment(User user, Long questionBoardArticleId, Long commentId, ReportDto reportDto);

    void favoriteQuestionBoardArticleComment(User user, Long questionBoardArticleId, Long commentId);

    void unfavoriteQuestionBoardArticleComment(User user, Long questionBoardArticleId, Long commentId);
}
