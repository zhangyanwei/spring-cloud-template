package com.github.zhangyanwei.sct.model.entity;

import com.github.zhangyanwei.sct.model.converter.RoleSetConverter;
import com.github.zhangyanwei.sct.model.security.Authority;
import com.github.zhangyanwei.sct.model.validation.Group.Save;
import com.github.zhangyanwei.sct.model.validation.UserIdentifier;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.EnumSet;

import static com.github.zhangyanwei.sct.common.utils.RegexPattern.*;
import static javax.persistence.EnumType.STRING;

// http://stackoverflow.com/questions/13012584/jpa-how-to-convert-a-native-query-result-set-to-pojo-class-collection
@Entity
@Table(name = "sct_user")
@UserIdentifier
public class User extends Base {

    private static final long serialVersionUID = 4015081435369839569L;

    @NotNull(groups = Save.class)
    @Pattern(regexp = REGEX_NICK_NAME)
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "name")
    private String name;

    @NotNull(groups = Save.class)
    @Column(name = "password")
    private String password;

    @Pattern(regexp = REGEX_MAIL)
    @Column(name = "email", unique = true)
    private String email;

    @Pattern(regexp = REGEX_PHONE)
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "status")
    @Enumerated(STRING)
    private UserStatus status;

    @Convert(converter = RoleSetConverter.class)
    @Column(name = "authorities")
    private EnumSet<Authority.Role> authorities;

    @Column(name = "last_access_time")
    private Date lastAccessTime;

    @Column(name = "registration_time")
    private Date registrationTime;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public EnumSet<Authority.Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(EnumSet<Authority.Role> authorities) {
        this.authorities = authorities;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum UserStatus {
        ACTIVE, LOCKED
    }
}