package d83t.bpmbackend.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonTypeName("errors")
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final int code;
    private final String message;

    public ErrorResponse(Error error){
        this.status = error.getStatus().value();
        this.error = error.getStatus().name();
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public ErrorResponse(int status, String error, String message){
        this.status = status;
        this.error = error;
        this.code = -1;
        this.message = message;
    }
}
