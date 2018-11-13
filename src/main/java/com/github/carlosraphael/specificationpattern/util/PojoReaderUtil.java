package com.github.carlosraphael.specificationpattern.util;

import org.springframework.util.StringUtils;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public final class PojoReaderUtil {

    private static final ConcurrentMap<Class, ConcurrentMap<String, Function>> CACHE = new ConcurrentHashMap<>();

    private PojoReaderUtil() {}

    public static ConcurrentMap<String, Function> loadUp(Class clazz) {
        return CACHE.computeIfAbsent(clazz, PojoReaderUtil::buildGetterMap);
    }

    public static <T> T getFieldValue(String fieldName, Object object) {
        ConcurrentMap<String, Function> getterMap = loadUp(object.getClass());
        Function getter = getterMap.getOrDefault(fieldName, o -> null);
        return (T) getter.apply(object);
    }

    private static ConcurrentMap<String, Function> buildGetterMap(Class clazz) {
        ConcurrentMap<String, Function> getterMap = new ConcurrentHashMap<>();
        Stream.of(clazz.getMethods())
                .filter(method -> method.getName().startsWith("get"))
                .forEach(method -> {
                    String fieldName = StringUtils.uncapitalize(method.getName().substring(3));
                    try {
                        getterMap.putIfAbsent(
                                fieldName,
                                (Function) getCallSite(clazz, method).getTarget().invokeExact()
                        );
                    } catch (Throwable e) {
                        throw new IllegalArgumentException("Lambda creation failed for method (" + method.getName() + ").", e);
                    }
                });
        return getterMap;
    }

    private static CallSite getCallSite(Class clazz, Method method)
            throws LambdaConversionException, NoSuchMethodException, IllegalAccessException {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        return LambdaMetafactory.metafactory(lookup, "apply",
                MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                lookup.findVirtual(clazz, method.getName(), MethodType.methodType(method.getReturnType())),
                MethodType.methodType(method.getReturnType(), clazz));
    }
}
