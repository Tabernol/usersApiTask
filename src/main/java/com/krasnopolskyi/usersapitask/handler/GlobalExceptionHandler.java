package com.krasnopolskyi.usersapitask.handler;

import com.krasnopolskyi.usersapitask.exception.GlobalAppException;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE = "Validation error. Check 'errors' field for details.";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Sorry, but something went wrong. Try again later";

    /**
     * Handles validation errors and builds a response with detailed error content.
     *
     * @param ex      The exception containing validation errors.
     * @param headers The headers for the response.
     * @param status  The HTTP status code for the response.
     * @param request The current web request.
     * @return ResponseEntity with a detailed error response for validation errors.
     */
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), VALIDATION_ERROR_MESSAGE);
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addErrorContent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Validation error occurred: ", ex);
        log.info("Response sent: " + errorResponse.getMessage() + errorResponse.getErrors());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    /**
     * Handles unknown exceptions and builds a response with a generic internal server error message.
     *
     * @param exception The unknown exception.
     * @param request   The current web request.
     * @return ResponseEntity with a generic internal server error response.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR_MESSAGE);
        log.error("Unknown error occurred", exception);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * Handles custom MinimumAgeException
     *
     * @param exception The MinimumAgeException
     * @param request   The current web request.
     * @return ResponseEntity with a response for MinimumAgeException.
     */
    @ExceptionHandler(MinimumAgeException.class)
    public ResponseEntity<Object> handleValidateAgeException(
            UserAppException exception, WebRequest request) {
        log.warn("caused by " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    /**
     * Handles ValidationException thrown during email validation.
     *
     * @param exception The ValidationException that was thrown.
     * @param request   The WebRequest associated with the request.
     * @return A ResponseEntity with a status of BAD_REQUEST and an ErrorResponse containing
     * the HTTP status code and the message from the ValidationException.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidateEmailException(
            GlobalAppException exception, WebRequest request) {
        log.warn("caused by " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }
    /**
     * Handles UserAppException thrown during user-related operations.
     *
     * @param exception The UserAppException that was thrown.
     * @param request   The WebRequest associated with the request.
     * @return A ResponseEntity with a status code specified by the exception, and an ErrorResponse
     * containing the HTTP status code and the message from the UserAppException.
     */
    @ExceptionHandler(UserAppException.class)
    public ResponseEntity<Object> handleUserAppException(
            UserAppException exception, WebRequest request) {
        log.warn("caused by " + exception.getMessage());
        return ResponseEntity.status(exception.getExceptionStatus()).body(
                new ErrorResponse(exception.getExceptionStatus(), exception.getMessage()));
    }
}
