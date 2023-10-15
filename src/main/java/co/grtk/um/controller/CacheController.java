package co.grtk.um.controller;

import co.grtk.um.utils.cache.MasterCacheData;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.GeneratedCacheKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor
@RestController
@FieldDefaults(level = PRIVATE, makeFinal = true)
@ConditionalOnProperty(name = "application.cache.enabled", havingValue = "true")
public class CacheController {
    IMap<String, GeneratedCacheKey> masterKeysMap;
    CacheManager cacheManager;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/cacheKeys")
    public ResponseEntity<List<MasterCacheData>> cacheKeys() {
        Set<Map.Entry<String, GeneratedCacheKey>> masterKeys = masterKeysMap.entrySet();
        List<MasterCacheData> masterCacheDataList = masterKeys.stream().map(entry -> new MasterCacheData(entry.getKey(), entry.getValue().toString())).toList();
        return new ResponseEntity<>(masterCacheDataList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/masterKeys")
    public ResponseEntity<Collection<String>> masterKeys(
            @RequestHeader(name = "userId") @NotEmpty String consultantEbhId) {
        Predicate predicate = Predicates.sql("__key like " + consultantEbhId + "%");
        Collection<String> masterKeys = masterKeysMap.keySet(predicate);
        return new ResponseEntity<>(masterKeys, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("/api/deleteCaches")
    public ResponseEntity<Collection<String>> deleteCaches(
            @RequestHeader(name = "userId") @NotEmpty String consultantEbhId) {
        long start = System.currentTimeMillis();
        Predicate predicate = Predicates.sql("__key like " + consultantEbhId + "%");
        Collection<String> masterKeys = masterKeysMap.keySet(predicate);
        long elapsed = System.currentTimeMillis() - start;
        log.info("consultantEbhId:{} call deleteCaches({}) predicate elapsed:{}",
                consultantEbhId, consultantEbhId, elapsed);
        masterKeys.stream().parallel()
                .map(masterKey -> {
                    String[] parts = masterKey.split("/");
                    String cacheName = parts[1];
                    return new MasterCacheData(masterKey, cacheName);
                }).forEach(masterCacheData -> {
                    Cache cache = cacheManager.getCache(masterCacheData.getCache());
                    GeneratedCacheKey generatedCacheKey = masterKeysMap.get(masterCacheData.getMasterKey());
                    cache.remove(generatedCacheKey);
                });
        elapsed = System.currentTimeMillis() - start;
        log.info("consultantEbhId:{} call deleteCaches({}) cacheRemove elapsed:{}",
                consultantEbhId, consultantEbhId, elapsed);

        masterKeysMap.removeAll(predicate);
        log.info("consultantEbhId:{} call deleteCaches({}) elapsed:{}",
                consultantEbhId, consultantEbhId, elapsed);
        return new ResponseEntity<>(masterKeys, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("/api/clearMasterKeysMap")
    public ResponseEntity<Collection<String>> clearMasterKeysMap(
            @RequestHeader(name = "userId") @NotEmpty String consultantEbhId) {
        Predicate predicate = Predicates.sql("__key like " + consultantEbhId + "%");
        Collection<String> masterKeys = masterKeysMap.keySet(predicate);
        masterKeysMap.removeAll(predicate);
        return new ResponseEntity<>(masterKeys, HttpStatus.OK);
    }
}