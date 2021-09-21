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

    private static final Map<Class<?>, List<Status>> statusMap = new HashMap<>();

    /**
     * 转换枚举，取出属性列表
     * @param classObj
     * @param <T>
     * @return
     */
    public static <T extends BaseStatus<?>> List<Status> getAll(Class<T> classObj) {
        if (statusMap.containsKey(classObj)) {
            return statusMap.get(classObj);
        }
        try {
            List<Status> list = new ArrayList<>();
            Method method = classObj.getMethod("values");
            BaseStatus<?>[] xx = (BaseStatus<?>[]) method.invoke(null);
            if (xx == null || xx.length == 0) {
                return list;
            }
            for (BaseStatus<?> t : xx) {
                list.add(t.getStatus());
            }
            statusMap.put(classObj, list);
            return list;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("", e);
            return null;
        }
    }
}
