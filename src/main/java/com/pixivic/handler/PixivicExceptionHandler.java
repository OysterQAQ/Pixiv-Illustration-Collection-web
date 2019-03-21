package com.pixivic.handler;

import com.pixivic.exception.CheckWordException;
import com.pixivic.exception.RankEmptyException;
import com.pixivic.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class PixivicExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity handleWebExchangeBindException(WebExchangeBindException webe) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result<>(webe.getFieldErrors().stream()
                .map(e -> e.getField() + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 +" "+ s2)));
    }

    @ExceptionHandler(CheckWordException.class)
    public ResponseEntity handleCheckWordException(CheckWordException cwe) {
        return ResponseEntity.status(cwe.getResult().getHttpStatus()).body(cwe.getResult());
    }

    @ExceptionHandler(RankEmptyException.class)
    public ResponseEntity handleRankEmptyException(RankEmptyException ree) {
        return ResponseEntity.status(ree.getResult().getHttpStatus()).body(ree.getResult());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result<>("请求参数错误"));
    }
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity handleServerWebInputException(ServerWebInputException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result<>("请求参数错误"));
    }

}
