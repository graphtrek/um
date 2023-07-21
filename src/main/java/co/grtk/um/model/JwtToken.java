package co.grtk.um.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "issuedAtUtcTime", columnList = "issuedAtUtcTime"),
        @Index(name = "subject", columnList = "subject"),
        @Index(name = "userName", columnList = "userName")  })
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(nullable = false)
    private int timePeriodMinutes;

    @Column(nullable = false)
    private Instant issuedAtUtcTime;

    @Column(nullable = false)
    private Instant expiresAtUtcTime;

    @Column(nullable = false)
    private String scope;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String userName;

    private String ip;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    @ManyToOne
    private UmUser umUser;

    public JwtToken(UmUser umUser, Jwt jwt, int timePeriodMinutes, String ip) {
        this.umUser = umUser;
        this.userName = umUser.getName();
        this.subject = umUser.getEmail();
        this.scope = jwt.getClaimAsString("scope");
        this.expiresAtUtcTime = jwt.getExpiresAt();
        this.issuedAtUtcTime = jwt.getIssuedAt();
        this.token = jwt.getTokenValue();
        this.ip = ip;
        this.timePeriodMinutes = timePeriodMinutes;
    }
}
