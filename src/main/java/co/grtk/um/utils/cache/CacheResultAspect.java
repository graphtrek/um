package co.grtk.um.utils.cache;

import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import javax.cache.annotation.CacheResult;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.reflect.Method;

import static lombok.AccessLevel.PRIVATE;

@Aspect
@Slf4j
@AllArgsConstructor
@Order
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CacheResultAspect {
    IMap<String, GeneratedCacheKey> masterKeysMap;

    @Around("@annotation(javax.cache.annotation.CacheResult)")
    public Object doStuff(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String cacheName = getCacheName(method);
        String cachedMethod = method.getName();
        Object[] args = joinPoint.getArgs();
        GeneratedCacheKey generatedCacheKey = TstCacheKeyGenerator.getCacheKey(method, args);
        String consultantEbhId = TstCacheKeyGenerator.getConsultantEbhId(method, args);

        log.info("consultantEbhId:{} not in cache:{} method:{} cacheKeyData:{}",
                consultantEbhId, cacheName, method.getName(), generatedCacheKey);

        Object proceed = joinPoint.proceed();

        proceedWithMasterCache(consultantEbhId, cacheName, cachedMethod, generatedCacheKey);

        long elapsed = System.currentTimeMillis() - start;
        log.info("consultantEbhId:{} PUT to cache:{} method:{} cacheKeyData:{} elapsed:{}",
                consultantEbhId,
                cacheName,
                method.getName(),
                generatedCacheKey,
                elapsed);
        return proceed;
    }

    private void proceedWithMasterCache(String consultantEbhId, String cacheName, String cachedMethod, GeneratedCacheKey generatedCacheKey) {
        long start = System.currentTimeMillis();
        String masterKey = consultantEbhId + "/" + cacheName + "/" + cachedMethod;
        masterKeysMap.put(masterKey, generatedCacheKey);
        long elapsed = System.currentTimeMillis() - start;
        log.info("consultantEbhId:{} PUT to cache:{} masterKey:{} generatedCacheKey:{} elapsed:{}",
                consultantEbhId,
                masterKeysMap.getName(),
                masterKey,
                generatedCacheKey,
                elapsed);
    }

    private String getCacheName(Method method) {
        CacheResult cacheResult = method.getAnnotation(CacheResult.class);
        String cacheName = cacheResult.cacheName();
        return cacheName;
    }
}