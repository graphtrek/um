package co.grtk.um.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UmUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UmUser> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<UmUserPrivilege> umUserPrivileges;

    public UmUserRole(String name) {
        this.name = name;
    }
}
