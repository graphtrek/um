package co.grtk.um.interceptor;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.um.config.AppConfig;
import co.grtk.um.service.KafkaPublisherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class ActivityLogInterceptor implements AsyncHandlerInterceptor {

    private final KafkaPublisherService kafkaPublisherService;
    private final AppConfig appConfig;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Capture end time of API call and calculate response time

        String token = request.getHeader("Authorization");
        if(appConfig.isKafkaProducerEnabled() && StringUtils.isNotBlank(token) && request.getRequestURI().startsWith("/api/")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            String currentPrincipalName = authentication.getName();
            UserActivityLogDTO userActivityLogDTO = new UserActivityLogDTO();
            userActivityLogDTO.setClientId(currentPrincipalName);
            userActivityLogDTO.setTimeStamp(LocalDateTime.now());
            userActivityLogDTO.setAppId(appConfig.getApplicationName());
            userActivityLogDTO.setEventId(UUID.randomUUID().toString());
            String activityCode = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1);
            userActivityLogDTO.setCategory(request.getMethod().toUpperCase());
            userActivityLogDTO.setLogLevel("INFO");
            userActivityLogDTO.setTextParams(authorities.toString());
            userActivityLogDTO.setActivityCode(activityCode.toUpperCase());
            userActivityLogDTO.setResultCode(String.valueOf(response.getStatus()));
            userActivityLogDTO.setToken(request.getHeader("Authorization"));
            kafkaPublisherService.logUserActivityAsync(userActivityLogDTO);
        }
    }
}