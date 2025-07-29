package com.eagle.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "mobileNo")
})
public class AppUser {

    @Id
    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private long mobileNo;

    private String firstName;
    private String middleName;
    private String lastName;
    private String profilePhoto;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_mapper",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<AppUserRole> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false)
    private Instant updatedDate;

    @Column(name = "updated_by")
    private String updateBy;

    public AppUser(String id, String email, String password, long mobileNo, String firstName, String middleName, String lastName, String profilePhoto, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Set<AppUserRole> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.roles = roles;
    }
}

