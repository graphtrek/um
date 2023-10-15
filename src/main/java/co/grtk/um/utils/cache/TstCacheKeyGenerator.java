package co.grtk.um.utils.cache;

import lombok.extern.slf4j.Slf4j;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class TstCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> context) {
        GeneratedCacheKey generatedCacheKey = getCacheKey(context.getMethod(), Arrays.stream(context.getKeyParameters()).map(CacheInvocationParameter::getValue).toArray());
        return generatedCacheKey;
    }

    public static TstGeneratedCacheKey getCacheKey(Method method, Object[] args) {
        if (method.getParameters().length == 0)
            throw new IllegalArgumentException(
                    "TstCacheKeyGenerator only works if the first parameter is consultantEbhId");

        TstUser tstUser = getTstUser(method);
        boolean partOfTheKey = tstUser.isPartOfTheKey();
        log.trace("consultantEbhId:{} first parameter is TstUser:{} isPartOfTheKey:{}",
                args[0], true, partOfTheKey);

        if (method.getParameters().length == 1 && tstUser.isPartOfTheKey() == false) {
            throw new IllegalArgumentException(
                    "TstCacheKeyGenerator only have one parameter" +
                            " but it is not part of the key," +
                            " so the cache key can not be generated");
        }

        if(!partOfTheKey)
            args = Arrays.copyOfRange(args, 1, args.length);

        Stream<Object> methodIdentity = Stream.of(method.getName());
        Stream<Object> parameterValues = Arrays.stream(args);
        return new TstGeneratedCacheKey(Stream.concat(methodIdentity, parameterValues).toArray());
    }

    public static TstUser getTstUser(Method method) {
        Parameter param = method.getParameters()[0];
        TstUser tstUser = param.getAnnotation(TstUser.class);
        if (Objects.isNull(tstUser))
            throw new IllegalArgumentException(
                    "TstCacheKeyGenerator only works if the first parameter is consultantEbhId and annotated with @TstUser annotation");
        return tstUser;
    }

    public static String getConsultantEbhId(Method method, Object[] args) {
        getTstUser(method);
        return (String) args[0];
    }

}