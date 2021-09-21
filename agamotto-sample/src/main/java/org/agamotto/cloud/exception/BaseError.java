package org.agamotto.cloud.exception;

import java.util.HashMap;
import java.util.Map;

public interface BaseError<T extends BaseError<?>> {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseError.class);

    Map<Integer, AgamottoException> mappings = new HashMap<>();

    default void toThrow() {
        throw getException();
    }

    AgamottoException getException();

    default void addException(T error) {

        if (mappings.containsKey(error.getException().getCode())) {
            log.error("异常{}({})定义重复", error.getException().getCode(), error.getException().getMessage());
            throw new RuntimeException("异常" + error.getException().getCode() + "(" + error.getException().getMessage() + ")定义重复");
        }
        mappings.put(error.getException().getCode(), error.getException());

    }

    default void addException(T[] errors) {
        for (T error : errors) {
            if (mappings.containsKey(error.getException().getCode())) {
                throw new RuntimeException("异常" + error.getException().getCode() + "(" + error.getException().getMessage() + ")定义重复");
            }
            mappings.put(error.getException().getCode(), error.getException());
        }
    }
}
