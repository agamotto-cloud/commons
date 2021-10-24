package org.agamotto.cloud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

@Slf4j
public class AgamottoWebAutoConfiguration {


    @Order(-2)
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ErrorAttributes errorAttributes,
                                                         WebProperties.Resources resourceProperties,
                                                         ServerProperties serverProperties,
                                                         ApplicationContext applicationContext,
                                                         ServerCodecConfigurer serverCodecConfigurer,
                                                         ObjectProvider<ViewResolver> viewResolversProvider) {
        return new GlobalExceptionHandler(errorAttributes, resourceProperties, serverProperties, applicationContext, serverCodecConfigurer, viewResolversProvider);
    }

}
