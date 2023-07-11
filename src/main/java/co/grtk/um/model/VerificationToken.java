package co.grtk.um.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"token"}))
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expirationTime;
    private static final int EXPIRATION_TIME = 10;
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    @OneToOne
    @JoinColumn(name = "principal_id")
    private UmUser umUser;

    public VerificationToken(String token, UmUser umUser) {
        super();
        this.token = token;
        this.umUser = umUser;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Instant getTokenExpirationTime() {
        Instant now = Instant.now();
        return now.plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
    }
}
