package co.grtk.um.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UmUserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MfaType mfaType = MfaType.NONE;

    private String secret;

    private String phone;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;
    public UmUser() {}

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UmUserRole> roles = new HashSet<>();

    public UmUser(String name, String email, String password, Set<UmUserRole> roles, UmUserStatus umUserStatus, String secret, MfaType mfaType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = umUserStatus;
        this.secret = secret;
        this.mfaType = mfaType;
    }


}
