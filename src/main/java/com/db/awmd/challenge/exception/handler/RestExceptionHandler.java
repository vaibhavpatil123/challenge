package com.db.awmd.challenge.exception.handler;

import com.db.awmd.challenge.exception.AccountAccessException;
import com.db.awmd.challenge.exception.AccountCreateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(AccountCreateException.class)
  protected ResponseEntity<Object> createAccException(
      AccountCreateException ex, WebRequest request) {
    ApiError apiError = new ApiError(BAD_REQUEST);

    apiError.setMessage(
        String.format("Failed to create account due to error '%s' ", ex.getCause()));
    apiError.setDebugMessage(ex.getMessage());

    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  /**
   * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
   *
   * @param ex the ConstraintViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
      javax.validation.ConstraintViolationException ex) {
    ApiError apiError = new ApiError(BAD_REQUEST);

    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getConstraintViolations());

    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(AccountAccessException.class)
  protected ResponseEntity<Object> handleEntityNotFound(AccountAccessException ex) {
    ApiError apiError = new ApiError(NOT_FOUND);

    apiError.setMessage(ex.getMessage());

    return buildResponseEntity(apiError);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();

    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

    return buildResponseEntity(
        new ApiError(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
  }

  @ExceptionHandler(InterruptedException.class)
  protected ResponseEntity<Object> handleInterruptedException(
      InterruptedException ex, WebRequest request) {
    ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);

    apiError.setMessage(String.format("Run time system exception '%s'", ex.getLocalizedMessage()));
    apiError.setDebugMessage(ex.getMessage());

    return buildResponseEntity(apiError);
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ApiError apiError = new ApiError(BAD_REQUEST);

    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());

    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    ApiError apiError = new ApiError(BAD_REQUEST);

    apiError.setMessage(
        String.format(
            "The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    apiError.setDebugMessage(ex.getMessage());

    return buildResponseEntity(apiError);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String error = ex.getParameterName() + " parameter is missing";

    return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
  }
}

// ~ Formatted by Jindent --- http://www.jindent.com
