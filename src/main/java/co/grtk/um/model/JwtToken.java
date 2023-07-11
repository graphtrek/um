package co.grtk.um.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String token;
    private Instant expirationTime;
    private String scope;
    private String subject;
    private String ip;
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    @ManyToOne
    private UmUser umUser;

    public JwtToken(UmUser umUser, String scope, Instant expirationTime, String token, String ip) {
        this.umUser = umUser;
        this.subject = umUser.getEmail();
        this.scope= scope;
        this.expirationTime =expirationTime;
        this.token = token;
        this.ip = ip;
    }

}
