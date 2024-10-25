package be.api.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User extends AbstractEntity {

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "emailConfirmed")
    private int emailConfirmed;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "googleUserId")
    private String googleUserId;

    @Column(name = "address")
    private String address;

    @Column(name = "isActive")
    private String isActive;

}
