package org.agamotto.cloud.web;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.exception.AgamottoException;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


/**
 * 〈统一异常处理〉
 *
 * @author
 * @date 2019/1/18
 */

@Slf4j
//@Component
public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {

    private final static String ERROR_EXCEPTION_KEY = "error_exception";

    /**
     *
     */
    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties.Resources resourceProperties,
                                  ServerProperties serverProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer,
                                  ObjectProvider<ViewResolver> viewResolversProvider) {
        super(errorAttributes, resourceProperties, serverProperties.getError(), applicationContext);

        this.setViewResolvers(viewResolversProvider.orderedStream()
                .collect(Collectors.toList()));
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions includeStackTrace) {
        Throwable error = super.getError(request);
        if (log.isDebugEnabled()) {
            log.error("", error);
        }
        if (error instanceof IllegalArgumentException) {
            return response(400, error.getMessage());
        }
        if (error instanceof AgamottoException) {
            return response(((AgamottoException) error).getCode(), error.getMessage());
        }
        if (error instanceof ResponseStatusException) {
            return response(((ResponseStatusException) error).getRawStatusCode(), this.buildMessage(request, error.getMessage()));
        }
        if (!log.isDebugEnabled()) {
            log.error("", error);
        }
        return response(500, this.buildMessage(request, "服务器内部异常"));
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.remove("code");
    }

    /**
     * 构建异常信息
     *
     * @param request
     * @param msg
     * @return
     */
    private String buildMessage(ServerRequest request, String msg) {
        if (msg == null) {
            msg = "";
        }
        return msg + " [" + request.methodName() +
                " " +
                request.uri() +
                " ]";
    }


    /**
     * 构建返回的JSON数据格式
     *
     * @param status       状态码
     * @param errorMessage 异常信息
     * @return
     */
    public static Map<String, Object> response(int status, String errorMessage) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", 200);
        map.put(ERROR_EXCEPTION_KEY, AgamottoResponse.error(status, errorMessage));
        return map;
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        return ServerResponse.status(getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error.get(ERROR_EXCEPTION_KEY)));
    }

}