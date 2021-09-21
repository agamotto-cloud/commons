package org.agamotto.cloud.status;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatusUtils {

    private static final Map<Class<?>, Map<Integer,Status>> statusMap = new HashMap<>();

    /**
     * 转换枚举，取出属性列表
     * @param classObj
     * @param <T>
     * @return
     */
    public static <T extends BaseStatus<?>> Map<Integer,Status> getAll(Class<T> classObj) {
        if (statusMap.containsKey(classObj)) {
            return statusMap.get(classObj);
        }
        try {
            Map<Integer,Status> statusMap = new HashMap<>();
            Method method = classObj.getMethod("values");
            BaseStatus<?>[] xx = (BaseStatus<?>[]) method.invoke(null);
            if (xx == null || xx.length == 0) {
                return statusMap;
            }
            for (BaseStatus<?> t : xx) {
                statusMap.put(t.value(),t.getStatus());
            }
            StatusUtils.statusMap.put(classObj, statusMap);
            return statusMap;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("", e);
            return null;
        }
    }
}
