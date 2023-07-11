package co.grtk.um.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Slf4j
@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames={"email"}),
        indexes = {
                @Index(name = "name_idx", columnList = "name"),
                @Index(name = "status_idx", columnList = "status")
        }
)
public class UmUser {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roles;
    @Enumerated(EnumType.STRING)
    private PrincipalStatus status;
    private String secret;
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;
    public UmUser() {}

    public UmUser(String name, String email, String password, String roles, PrincipalStatus principalStatus, String secret) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = principalStatus;
        this.secret = secret;
    }


}
