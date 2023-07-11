package co.grtk.um.listener;

import co.grtk.um.model.UmUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MailEvent extends ApplicationEvent {
    private UmUser umUser;
    private String applicationUrl;
    private MailType mailType;

    public MailEvent(MailType mailType, UmUser umUser, String applicationUrl) {
        super(umUser);
        this.umUser = umUser;
        this.applicationUrl = applicationUrl;
        this.mailType = mailType;
    }
}
