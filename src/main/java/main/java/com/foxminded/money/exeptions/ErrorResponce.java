package main.java.com.foxminded.money.exeptions;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public class ErrorResponce {
    @Schema(description = "HttpStatus: BAD_REQUEST, INTERNAL_SERVER_ERROR, etc.",
            example = "BAD_REQUEST")
    private HttpStatus httpStatusCode;

    @Schema(description = "Error Message",
            example = "findTeacher.id: 111-222-333-444  isn't correct  UUID. See RFC 4122 - 4.1. Format")
    private String message;

    @Schema(description = "Error Description",
            example = "The server cannot or will not process the request due to something that is perceived " +
                    "to be a client error (e.g., malformed request syntax, invalid request message framing, " +
                    "or deceptive request routing).")
    private String description;

    public ErrorResponce(HttpStatus httpStatusCode, String message, String description) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.description = description;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatus httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
