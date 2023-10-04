package co.grtk.um.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class RequestContextService {
    public static class RequestContext {
        protected Map<String,String> headers;
        protected String uri;
        protected String name;
    }

    private static final InheritableThreadLocal<RequestContext> requestContext = new InheritableThreadLocal<>() {
        @Override
        protected RequestContext initialValue() {
            return new RequestContext();
        }
    };

    public void setContextFromRequest(HttpServletRequest request) {
        requestContext.get().name = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1);
        requestContext.get().uri = request.getRequestURI();
        requestContext.get().headers = getHeadersInfo(request);
    }

    public void clearContext() {
        requestContext.set(new RequestContext());
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key =  headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
