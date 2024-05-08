package roomescape.controller.advice;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.auth.application.AuthorizationException;
import roomescape.exception.ReservationBusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "잘못된 형식의 Request Body 입니다."
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);

        final String errorMessage = e.getFieldErrors().stream()
                .map(error -> error.getField() + "는 " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        errorMessage
                ));
    }

    @ExceptionHandler(ReservationBusinessException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(final ReservationBusinessException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(final Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 오류입니다."
                ));
    }
}
