package com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails;

import com.github.zhangyanwei.sct.service.UserService;
import com.github.zhangyanwei.sct.service.dto.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userService.tryFindByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException(format("Not found user by phone [%s].", phone)));
        return new UserDetails(user);
    }
}
