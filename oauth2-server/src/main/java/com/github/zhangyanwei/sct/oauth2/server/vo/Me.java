package com.github.zhangyanwei.sct.oauth2.server.vo;

import com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

public class Me {

    private String nickname;
    private String mobile;
    private String avatar;
    private String[] authorities;
    private Boolean temporary;
    private Boolean subscribed;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public static Me from(UserDetails details) {
        Me me = new Me();
        // only set it when temporary is true, so not set it like "me.setTemporary(details.getUser() == null)"
        if (details.getUser() == null) {
            me.setTemporary(true);
        }

        me.setNickname(details.getNickname());
        me.setMobile(details.getMobile());
        me.setAvatar(details.getAvatar());
        me.setAuthorities(details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        Boolean subscribe = (Boolean) details.getDetails().get("subscribe");
        if (subscribe != null) {
            me.setSubscribed(subscribe);
        }
        return me;
    }
}
