package co.grtk.um.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Setter
@Table(name = "users",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"email"}))
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String roles;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User() {}

    public User(String username, String email, String password, String roles, UserStatus userStatus) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.userStatus = userStatus;
    }


}
