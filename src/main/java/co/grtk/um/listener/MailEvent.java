package co.grtk.um.listener;

import co.grtk.um.model.Principal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MailEvent extends ApplicationEvent {
    private Principal principal;
    private String applicationUrl;
    private MailType mailType;

    public MailEvent(MailType mailType, Principal principal, String applicationUrl) {
        super(principal);
        this.principal = principal;
        this.applicationUrl = applicationUrl;
        this.mailType = mailType;
    }
}
