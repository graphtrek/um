package co.grtk.um.config;

import co.grtk.um.utils.cache.CacheResultAspect;
import co.grtk.um.utils.cache.TstGeneratedCacheKey;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.annotation.GeneratedCacheKey;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@ConditionalOnProperty(name = "application.cache.enabled", havingValue = "true")
public class CacheConfiguration {

    @Bean
    public IMap<String, GeneratedCacheKey> masterKeysMap(HazelcastInstance instance) {
        IMap<String, GeneratedCacheKey> masterKeysMap = instance.getMap("masterKeysMap");
        return masterKeysMap;
    }

    @Bean
    public CacheResultAspect getCacheResultAspect(IMap<String, GeneratedCacheKey> masterKeysMap) {
        return new CacheResultAspect(masterKeysMap);
    }

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = ClientConfig.load();
        clientConfig.setClassLoader(TstGeneratedCacheKey.class.getClassLoader());
        return clientConfig;
    }

}