package org.agamotto.cloud.web;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.exception.AgamottoException;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.agamotto.cloud.resp.Ret;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Ret<?> defaultErrorHandler(Exception e) {
        log.error("未知错误", e);
        return AgamottoResponse.error(500, e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Ret<?> defaultErrorHandler(Throwable e) {
        log.error("未知错误", e);
        return AgamottoResponse.error(500, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AgamottoException.class)
    public Ret<?> defaultErrorHandler(AgamottoException e) {
        if (log.isDebugEnabled()) {
            log.debug("", e);
        }
        return AgamottoResponse.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Ret<?> defaultErrorHandlerxx(RuntimeException e) {
        log.error("未知错误",e);
        return AgamottoResponse.error(500, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MissingServletRequestPartException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public final Ret handleException(Exception ex, WebRequest request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("", ex);
        }
        return AgamottoResponse.error(500, ex.getMessage());
    }

    /**
     * 参数校验异常(校验RequestBody类型 参数)
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,BindException.class})
    @ResponseBody
    public Ret<?> allExceptionHandler(MethodArgumentNotValidException e) {
        if (log.isDebugEnabled()) {
            log.debug("", e);
        }
        if (e.getBindingResult().getAllErrors().size() > 0) {
            String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            return AgamottoResponse.error(500, errorMessage);
        } else {
            return AgamottoResponse.error(500, e.getMessage());
        }
    }

    /**
     * 参数绑定
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Ret<?> listExceptionHandler(ConstraintViolationException e) {
        if (log.isDebugEnabled()) {
            log.debug("", e);
        }
        return AgamottoResponse.error(500, e.getMessage());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Ret<?> inputExceptionHandler(MissingServletRequestParameterException e) {
        if (log.isDebugEnabled()) {
            log.debug("", e);
        }
        return AgamottoResponse.error(500, e.getMessage());
    }




}