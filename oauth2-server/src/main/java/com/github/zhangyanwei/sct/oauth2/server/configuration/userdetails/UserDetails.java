package com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails;

import com.github.zhangyanwei.sct.model.security.Authority;
import com.github.zhangyanwei.sct.service.dto.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.github.zhangyanwei.sct.model.entity.User.UserStatus.ACTIVE;

public class UserDetails implements XUserDetails {

    private static final long serialVersionUID = -7966053625918135698L;

    private XUserDetails userDetails;

    public UserDetails(User user) {
        this.userDetails = new DBUser(user);
    }

    public User getUser() {
        return userDetails.getUser();
    }

    @Override
    public String getNickname() {
        return userDetails.getNickname();
    }

    @Override
    public String getAvatar() {
        return userDetails.getAvatar();
    }

    @Override
    public String getMobile() {
        return userDetails.getMobile();
    }

    @Override
    public Map<String, ?> getDetails() {
        return userDetails.getDetails();
    }

    public Long getId() {
        return userDetails.getId();
    }

    public XUserDetails getProxiedDetails() {
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getUsername() {
        return userDetails.getUsername();
    }

    @Override
    public String getPassword() {
        return userDetails.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDetails.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userDetails.isEnabled();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;

        } else if (!(obj instanceof UserDetails)) {
            return false;
        }

        return this.getUsername().equals(((UserDetails) obj).getUsername());
    }

    public static void set(UserDetails details) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, "N/A", details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static abstract class AbstractUserDetails implements XUserDetails {

        private static final long serialVersionUID = -1330379874006780008L;

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

    }

    public static final class DBUser extends AbstractUserDetails {

        private static final long serialVersionUID = 419461503984400649L;

        private User user;

        DBUser(User user) {
            this.user = user;
        }

        @Override
        public Long getId() {
            return user.getId();
        }

        @Override
        public User getUser() {
            return user;
        }

        @Override
        public String getNickname() {
            return user.getNickname();
        }

        @Override
        public String getAvatar() {
            return null;
        }

        @Override
        public String getMobile() {
            return user.getMobile();
        }

        @Override
        public Map<String, ?> getDetails() {
            return new HashMap<>();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList(user.getAuthorities().stream().map(Authority.Role::authority).toArray(String[]::new));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return String.valueOf(user.getId());
        }

        @Override
        public boolean isAccountNonLocked() {
            return user.getStatus() == ACTIVE;
        }

    }
}