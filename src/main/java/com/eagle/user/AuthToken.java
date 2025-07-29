package com.eagle.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_token")
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "auth_token",length = 10000)
    private String tokenValue;

//    @Column(name = "token_value")
//    private String tokenValue;

    @Column(name = "token_revoked")
    private boolean tokenRevoked;

    @Column(name = "token_expired")
    private boolean tokenExpired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @CreationTimestamp
    @Column(name = "created_date")
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date",insertable = false)
    private Instant updatedDate;

    public AuthToken(String authToken, boolean tokenRevoked, boolean tokenExpired, AppUser appUser) {
    }
}
