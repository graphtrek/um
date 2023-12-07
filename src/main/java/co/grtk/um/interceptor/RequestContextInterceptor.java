package co.grtk.um.interceptor;

import co.grtk.um.service.RequestContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@AllArgsConstructor
@Slf4j
@Component
public class RequestContextInterceptor implements HandlerInterceptor {
    private final RequestContextService requestContextService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Request received for URL: " + request.getRequestURI());
        requestContextService.setContextFromRequest(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
               @Nullable ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        long elapsed = requestContextService.elapsed();
        requestContextService.clearContext();

        log.info("Request processing completed for URL: {}. Total Time Taken: {} ms user: {}",
                request.getRequestURI() , elapsed , currentPrincipalName);
    }
}
