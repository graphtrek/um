package co.grtk.um.config;

import co.grtk.um.interceptor.ActivityLogInterceptor;
import co.grtk.um.interceptor.RequestContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ActivityLogInterceptor activityLogInterceptor;
    private final RequestContextInterceptor requestContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor);
        registry.addInterceptor(activityLogInterceptor);
    }
}