package com.emall.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 类型转换工具类
 */
public class ClassCastUtil {
    /**
     * 通过反射将初始对象转换成目标对象
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> T classCast(Object source, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T target = clazz.newInstance();
        Class sourceClazz = source.getClass();

        Field[] sourceFields = sourceClazz.getDeclaredFields();
        Field[] targetFields = clazz.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            for (Field targetField : targetFields) {
                if (targetField.getName().equals(sourceField.getName()) && targetField.getType() == sourceField.getType()) {
                    int mod = targetField.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                    break;
                }
            }
        }

        return target;
    }
}
