package com.kulsin.wallet.exception;

import com.kulsin.wallet.core.account.exception.AccountServiceException;
import com.kulsin.wallet.core.transaction.exception.TransactionServiceException;
import com.kulsin.wallet.model.response.WalletErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;

@Slf4j
@ControllerAdvice
public class WalletExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {

        log.error("Unexpected exception occurred in wallet", exception);

        if (exception instanceof WalletException) {

            return badRequest().body(walletErrorResponse(BAD_REQUEST, exception.getMessage()));

        } else if (exception instanceof AccountServiceException || exception instanceof TransactionServiceException) {
            return internalServerError().body(walletErrorResponse(INTERNAL_SERVER_ERROR, exception.getMessage()));

        }

        return internalServerError().body(walletErrorResponse(INTERNAL_SERVER_ERROR, exception.getMessage()));

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String message = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        return badRequest().body(walletErrorResponse(BAD_REQUEST, message));
    }

    private WalletErrorResponse walletErrorResponse(HttpStatus status, String message) {

        return WalletErrorResponse.builder()
                .statusCode(String.valueOf(status.value()))
                .message(message)
                .build();

    }

}
