package co.grtk.um.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "JWT_TOKEN")
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String token;
    private Instant expirationTime;
    private String scope;
    private String subject;
    private static final int EXPIRATION_TIME = 5;
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    @OneToOne
    @JoinColumn(name = "principal_id")
    private Principal principal;

    public JwtToken(Principal principal, String scope, Instant expirationTime, String token) {
        this.principal = principal;
        this.subject = principal.getEmail();
        this.scope= scope;
        this.expirationTime =expirationTime;
        this.token = token;
    }
}
