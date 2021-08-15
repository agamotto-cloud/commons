package org.agamotto.cloud.web;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.exception.AgamottoException;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.agamotto.cloud.resp.Ret;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Ret defaultErrorHandler(Exception e) {
        log.error("未知错误", e);
        return AgamottoResponse.error(500, e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = AgamottoException.class)
    public Ret defaultErrorHandler(AgamottoException e) {
        return AgamottoResponse.error(e.getCode(), e.getMessage());
    }



}