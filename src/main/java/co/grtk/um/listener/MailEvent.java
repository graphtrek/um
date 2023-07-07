package co.grtk.um.listener;

import co.grtk.um.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MailEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
    private MailType mailType;

    public MailEvent(MailType mailType, User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.mailType = mailType;
    }
}
