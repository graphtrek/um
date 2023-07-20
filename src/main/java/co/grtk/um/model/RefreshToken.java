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
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"token"}))
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  @JoinColumn(name = "umuser_id")
  private UmUser umUser;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private int timePeriodMinutes;

  @Column(nullable = false)
  private Instant issuedAtUtcTime;

  @CreationTimestamp(source = SourceType.DB)
  private Instant createdAt;

  @UpdateTimestamp(source = SourceType.DB)
  private Instant updatedAt;

  public RefreshToken(UmUser umUser, int timePeriodMinutes) {
    super();
    this.token = UUID.randomUUID().toString();
    this.umUser = umUser;
    this.timePeriodMinutes = timePeriodMinutes;
    this.issuedAtUtcTime = Instant.now();
  }
  public Instant getTokenExpirationTime() {
    return issuedAtUtcTime.plus(timePeriodMinutes, ChronoUnit.MINUTES);
  }
}
