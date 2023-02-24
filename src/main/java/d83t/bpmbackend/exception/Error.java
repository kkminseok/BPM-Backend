package d83t.bpmbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {
    NOT_FOUND_USER_ID("kakao user not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXITS("user already exits", HttpStatus.CONFLICT),
    USER_NICKNAME_ALREADY_EXITS("user nickname already exits", HttpStatus.CONFLICT),
    S3_UPLOAD_FAIL("upload fail", HttpStatus.INTERNAL_SERVER_ERROR),
    S3_GET_FILE_FAIL("fail to get file", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_USER_NICK_NAME("duplicate user nickname", HttpStatus.CONFLICT),
    FILE_SIZE_MAX("a Maximum of 5 files can Come in", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    Error(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
