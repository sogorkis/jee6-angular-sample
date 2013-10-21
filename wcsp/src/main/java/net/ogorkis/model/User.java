package net.ogorkis.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;
import static net.ogorkis.wcsp.config.Constants.MD5_BASE64_COLUMN_LENGTH;
import static net.ogorkis.wcsp.config.Constants.REMEMBER_ME_TOKEN_LENGTH;

@XmlRootElement
@XmlAccessorType(FIELD)
@Entity
@Table(name = "users")
public class User {

    @XmlTransient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Length(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Length(min = 5, max = 255)
    @Column(length = 255)
    private String name;

    @XmlTransient
    @NotNull
    @Length(max = MD5_BASE64_COLUMN_LENGTH)
    @Column(name = "password_hash", length = MD5_BASE64_COLUMN_LENGTH, nullable = false)
    private String passwordHash;

    @XmlTransient
    @NotNull
    @Length(max = MD5_BASE64_COLUMN_LENGTH)
    @Column(name = "password_salt", length = MD5_BASE64_COLUMN_LENGTH, nullable = false)
    private String passwordSalt;

    @XmlTransient
    @Column(name = "remember_me_token", length = REMEMBER_ME_TOKEN_LENGTH)
    private String rememberMeToken;

    @XmlTransient
    @Column(name = "remember_me_expiration")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime rememberMeExpiration;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    private Set<UserRole> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = rememberMeToken;
    }

    public DateTime getRememberMeExpiration() {
        return rememberMeExpiration;
    }

    public void setRememberMeExpiration(DateTime rememberMeExpiration) {
        this.rememberMeExpiration = rememberMeExpiration;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .add("email", email)
                .toString();
    }
}
