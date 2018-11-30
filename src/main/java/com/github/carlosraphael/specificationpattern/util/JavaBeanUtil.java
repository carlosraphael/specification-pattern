package com.github.carlosraphael.specificationpattern.util;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@SuppressWarnings("unchecked")
public final class JavaBeanUtil {

    private static final Pattern FIELD_SEPARATOR = Pattern.compile("\\.");
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final ClassValue<Map<String, Function>> CACHE = new ClassValue<Map<String, Function>>() {
        @Override
        protected Map<String, Function> computeValue(Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private JavaBeanUtil() {}

    public static <T> T getFieldValue(Object object, String fieldName) {
        return (T) getCachedFunction(object.getClass(), fieldName).apply(object);
    }

    private static Function getCachedFunction(Class<?> objectClass, String fieldName) {
        final Function function = CACHE.get(objectClass).get(fieldName);
        if (function == null) {
            return createAndCacheFunction(objectClass, fieldName);
        }
        return function;
    }

    private static Function createAndCacheFunction(Class<?> objectClass, String path) {
        return cacheAndGetFunction(path, objectClass,
                createFunction(objectClass, path)
                        .stream()
                        .reduce(Function::andThen)
                        .orElseThrow(IllegalStateException::new)
        );
    }

    private static Function cacheAndGetFunction(String path, Class<?> objectClass, Function functionToBeCached) {
        Function cachedFunction = CACHE.get(objectClass).putIfAbsent(path, functionToBeCached);
        return cachedFunction != null ? cachedFunction : functionToBeCached;
    }

    private static List<Function> createFunction(Class<?> objectClass, String path) {
        String[] fieldNames = FIELD_SEPARATOR.split(path);
        Class nestedClass = null;
        List<Function> functions = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Tuple2<? extends Class, Function> getFunction =
                    createGetFunction(fieldName, nestedClass != null ? nestedClass : objectClass);
            nestedClass = getFunction._1;
            functions.add(getFunction._2);
        }
        return functions;
    }

    private static Tuple2<? extends Class, Function> createGetFunction(String fieldName, Class objectClass) {
        return Stream.of(objectClass.getDeclaredMethods())
                .filter(JavaBeanUtil::isGetterMethod)
                .filter(method -> StringUtils.endsWithIgnoreCase(method.getName(), fieldName))
                .map(JavaBeanUtil::createTupleContainingReturnTypeAndGetter)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private static boolean isGetterMethod(Method method) {
        return method.getParameterCount() == 0 &&
                !Modifier.isStatic(method.getModifiers()) &&
                method.getName().startsWith("get") &&
                !method.getName().endsWith("Class");
    }

    private static Tuple2<? extends Class, Function> createTupleContainingReturnTypeAndGetter(Method method) {
        try {
            return Tuple.of(
                    method.getReturnType(),
                    (Function) createGetterCallSite(LOOKUP.unreflect(method)).getTarget().invokeExact()
            );
        } catch (Throwable e) {
            throw new IllegalArgumentException("Lambda creation failed for method (" + method.getName() + ").", e);
        }
    }

    private static CallSite createGetterCallSite(MethodHandle handle) throws LambdaConversionException {
        return LambdaMetafactory.metafactory(LOOKUP, "apply",
                MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                handle, handle.type());
    }
}
