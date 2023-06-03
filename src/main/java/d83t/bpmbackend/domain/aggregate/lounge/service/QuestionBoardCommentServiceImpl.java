package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.base.report.repository.ReportRepository;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentResponse;
import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardCommentUpdateDto;
import d83t.bpmbackend.domain.aggregate.lounge.entity.*;
import d83t.bpmbackend.domain.aggregate.lounge.repository.*;
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

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionBoardCommentServiceImpl implements QuestionBoardCommentService {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardCommentQueryDSLRepository questionBoardCommentQueryDSLRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final QuestionBoardCommentFavoriteRepository questionBoardCommentFavoriteRepository;

    @Override
    public QuestionBoardCommentResponse createComment(User user, Long questionBoardArticleId, QuestionBoardCommentDto commentDto) {
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardArticleId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_ARTICLE);
        });
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });
        QuestionBoardComment parent = null;
        //자식 댓글인 경우
        if (commentDto.getParentId() != null) {
            parent = questionBoardCommentRepository.findById(commentDto.getParentId()).orElseThrow(() -> {
                throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_COMMENT_PARENT_ID);
            });
            if (parent.getQuestionBoard().getId() != questionBoardArticleId) {
                throw new CustomException(Error.DIFF_POST_CHILD_ID_PARENT_ID);
            }
        }

        Profile profile = findUser.getProfile();

        QuestionBoardComment questionBoardComment = QuestionBoardComment.builder()
                .questionBoard(questionBoard)
                .author(profile)
                .body(commentDto.getBody())
                .build();

        if (parent != null) {
            questionBoardComment.updateParent(parent);
        }

        QuestionBoardComment comment = questionBoardCommentRepository.save(questionBoardComment);

        return convertComment(user, comment);
    }

    @Override
    public List<QuestionBoardCommentResponse> getComments(User user, Long questionBoardArticleId) {
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardArticleId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_ARTICLE);
        });
        List<QuestionBoardComment> comments = questionBoardCommentQueryDSLRepository.findAllByQuestionComment(questionBoard).stream()
                .filter(questionBoardComment -> questionBoardComment.getQuestionBoard().getId().equals(questionBoardArticleId)
                ).collect(Collectors.toList());

        List<QuestionBoardCommentResponse> result = new ArrayList<>();
        Map<Long, QuestionBoardCommentResponse> map = new HashMap<>();

        comments.stream().forEach(c -> {
            QuestionBoardCommentResponse cdto = convertComment(user, c);
            map.put(c.getId(), cdto);
            if (c.getParent() != null) map.get(c.getParent().getId()).addChildren(cdto);
            else result.add(cdto);
        });
        return result;
    }

    @Override
    public QuestionBoardCommentResponse updateComment(User user, Long questionBoardArticleId, Long commentId, QuestionBoardCommentUpdateDto questionBoardCommentUpdateDto) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findByQuestionBoardIdAndId(questionBoardArticleId, commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_OR_COMMENT);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();
        //작성자인지 확인
        if (!questionBoardComment.getAuthor().getId().equals(profile.getId())) {
            throw new CustomException(Error.NOT_MATCH_USER);
        }

        questionBoardComment.updateBody(questionBoardCommentUpdateDto.getBody());

        questionBoardCommentRepository.save(questionBoardComment);

        return convertComment(user, questionBoardComment);
    }

    @Override
    public void deleteComment(User user, Long questionBoardArticleId, Long commentId) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findByQuestionBoardIdAndId(questionBoardArticleId, commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_OR_COMMENT);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();
        //작성자인지 확인
        if (!questionBoardComment.getAuthor().getId().equals(profile.getId())) {
            throw new CustomException(Error.NOT_MATCH_USER);
        }
        questionBoardCommentRepository.delete(questionBoardComment);
    }

    @Override
    public void reportComment(User user, Long questionBoardArticleId, Long commentId, ReportDto reportDto) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findByQuestionBoardIdAndId(questionBoardArticleId, commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_OR_COMMENT);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        //신고 3회 삭제.
        if (questionBoardComment.getReportCount() >= 2) {
            questionBoardCommentRepository.delete(questionBoardComment);
        } else {
            questionBoardComment.plusReport();
            questionBoardCommentRepository.save(questionBoardComment);
        }

        //로그성 테이블에 남기기
        Report report = Report.builder()
                .commentAuthor(questionBoardComment.getAuthor().getNickName())
                .commentBody(questionBoardComment.getBody())
                .commentId(questionBoardComment.getId())
                .commentCreatedAt(questionBoardComment.getCreatedDate())
                .commentUpdatedAt(questionBoardComment.getModifiedDate())
                .reportReason(reportDto.getReason())
                .type("question comment")
                .reporter(findUser.getProfile().getId())
                .build();

        reportRepository.save(report);

    }

    @Override
    public void favoriteQuestionBoardArticleComment(User user, Long questionBoardArticleId, Long commentId) {
        questionBoardRepository.findById(questionBoardArticleId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_ARTICLE);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_COMMENT);
        });

        questionBoardCommentFavoriteRepository.findByQuestionBoardCommentIdAndUserId(questionBoardComment.getId(), user.getId()).ifPresent(e -> {
            throw new CustomException(Error.ALREADY_FAVORITE);
        });

        QuestionBoardCommentFavorite favorite = QuestionBoardCommentFavorite.builder()
                .questionBoardComment(questionBoardComment)
                .user(findUser)
                .build();
        questionBoardCommentFavoriteRepository.save(favorite);
        convertComment(findUser, questionBoardComment);
    }

    @Override
    public void unfavoriteQuestionBoardArticleComment(User user, Long questionBoardArticleId, Long commentId) {
        questionBoardRepository.findById(questionBoardArticleId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_ARTICLE);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_QUESTION_BOARD_COMMENT);
        });
        QuestionBoardCommentFavorite favorite = questionBoardCommentFavoriteRepository.findByQuestionBoardCommentIdAndUserId(questionBoardComment.getId(), user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.ALREADY_UN_FAVORTIE);
        });

        questionBoardCommentFavoriteRepository.delete(favorite);
        convertComment(findUser, questionBoardComment);
    }

    private QuestionBoardCommentResponse convertComment(User user, QuestionBoardComment questionBoardComment) {

        ProfileResponse profile = profileService.getProfile(questionBoardComment.getAuthor().getId());
        if (questionBoardComment.getParent() != null) {
            return QuestionBoardCommentResponse.builder()
                    .id(questionBoardComment.getId())
                    .author(QuestionBoardCommentResponse.Author.builder()
                            .id(profile.getId())
                            .nickname(profile.getNickname())
                            .profilePath(profile.getImage()).build())
                    .body(questionBoardComment.getBody())
                    .reportCount(questionBoardComment.getReportCount())
                    .parentId(questionBoardComment.getParent().getId())
                    .favorited(getFavoritesStatus(user, questionBoardComment))
                    .favoritesCount(getFavoritesCount(questionBoardComment.getId()))
                    .createdAt(questionBoardComment.getCreatedDate())
                    .updatedAt(questionBoardComment.getModifiedDate())
                    .build();
        } else {
            return QuestionBoardCommentResponse.builder()
                    .id(questionBoardComment.getId())
                    .author(QuestionBoardCommentResponse.Author.builder()
                            .id(profile.getId())
                            .nickname(profile.getNickname())
                            .profilePath(profile.getImage()).build())
                    .body(questionBoardComment.getBody())
                    .reportCount(questionBoardComment.getReportCount())
                    .favorited(getFavoritesStatus(user, questionBoardComment))
                    .favoritesCount(getFavoritesCount(questionBoardComment.getId()))
                    .createdAt(questionBoardComment.getCreatedDate())
                    .updatedAt(questionBoardComment.getModifiedDate())
                    .build();
        }
    }

    private Boolean getFavoritesStatus(User user, QuestionBoardComment questionBoardComment) {
        if (user == null) return false;
        Optional<QuestionBoardCommentFavorite> favoriteStatus = questionBoardCommentFavoriteRepository.findByQuestionBoardCommentIdAndUserId(questionBoardComment.getId(), user.getId());
        return favoriteStatus.isEmpty() ? false : true;
    }

    private Long getFavoritesCount(Long commentId) {
        return questionBoardCommentFavoriteRepository.countByQuestionBoardCommentId(commentId);
    }
}
