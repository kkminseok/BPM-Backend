package d83t.bpmbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {

    //공통
    S3_UPLOAD_FAIL("파일 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR, 0),
    S3_GET_FILE_FAIL("파일을 가져올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR, 1),
    FILE_SIZE_MAX("최대 5개의 파일을 올릴 수 있습니다.", HttpStatus.BAD_REQUEST, 2),
    INVALID_REQUEST("잘못된 요청입니다. 요청을 확인해주세요.", HttpStatus.BAD_REQUEST, 3),
    NOT_MATCH_USER("해당 게시글을 작성한 유저가 아닙니다.", HttpStatus.CONFLICT, 4),
    FILE_REQUIRED("최소 1개 이상의 이미지를 첨부해야합니다.", HttpStatus.NOT_FOUND, 5),
    ALREADY_FAVORITE("이미 좋아요를 눌렀습니다.", HttpStatus.CONFLICT, 6),
    ALREADY_UN_FAVORTIE("이미 좋아요를 취소하였습니다.", HttpStatus.CONFLICT, 7),
    ALREADY_REPORT("이미 신고를 한 상태입니다.", HttpStatus.CONFLICT,28),
    //인증 관련
    UNAUTHORIZED_USER("인가되지 않은 유저입니다.", HttpStatus.UNAUTHORIZED, 8),
    //파일 변환과정에서 실패했을때
    FILE_TRANSFER_FAIL("파일 변환에 실패하였습니다. 관리자에게 문의해주세요.", HttpStatus.INTERNAL_SERVER_ERROR, 9),

    //유저 도메인
    NOT_FOUND_USER_ID("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 10),
    NOT_FOUND_KAKAO_ID("등록된 카카오 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 11),
    NOT_FOUND_PROFILE("등록된 유저 프로필을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 12),
    USER_ALREADY_EXITS("이미 등록된 카카오 계정입니다.", HttpStatus.CONFLICT, 13),
    USER_NICKNAME_ALREADY_EXITS("이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT, 14),
    //스케줄 관련
    NOT_FOUND_SCHEDULE("등록된 스케줄을 찾을 수 없습니다.", HttpStatus.NO_CONTENT, 15),
    USER_ALREADY_REGISTER_SCHEDULE("이미 스케줄을 등록하였습니다.", HttpStatus.BAD_REQUEST, 16),

    //눈바디 관련
    NOT_FOUND_BODY_SHAPE("등록된 내눈바디 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 17),

    //스튜디오 관련
    NOT_FOUND_STUDIO("스튜디오를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 18),
    NOT_FOUND_SCRAP("스크랩을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 19),

    //리뷰 관련
    NOT_FOUND_REVIEW("리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 20),

    //질문하기 게시판 관련
    NOT_FOUND_QUESTION_ARTICLE("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 21),
    NOT_FOUND_QUESTION_BOARD_COMMENT("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 22),
    NOT_FOUND_QUESTION_BOARD_OR_COMMENT("댓글이나 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 23),
    NOT_FOUND_QUESTION_BOARD_COMMENT_PARENT_ID("해당 대댓글의 원본댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 24),
    DIFF_POST_CHILD_ID_PARENT_ID("해당 댓글에 문제가 발생하였습니다. 관리자에게 문의해주세요.", HttpStatus.BAD_REQUEST, 25),

    //커뮤니티 게시판 관련
    NOT_FOUND_COMMUNITY("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 26),
    NOT_FOUND_COMMUNITY_COMMENT("해당 게시글의 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 27);


    private final String message;
    private final HttpStatus status;
    private final int code;


    Error(String message, HttpStatus status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

}
