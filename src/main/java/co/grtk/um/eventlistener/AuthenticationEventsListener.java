package co.grtk.um.eventlistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventsListener {
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.info("AuthenticationSuccessEvent {}", success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        log.warn("AbstractAuthenticationFailureEvent {}", failures.getAuthentication().getName());
    }
}
