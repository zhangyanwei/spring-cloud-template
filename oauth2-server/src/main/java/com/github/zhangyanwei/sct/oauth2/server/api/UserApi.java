package com.github.zhangyanwei.sct.oauth2.server.api;

import com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails.UserDetails;
import com.github.zhangyanwei.sct.oauth2.server.vo.Me;
import com.github.zhangyanwei.sct.service.UserService;
import com.github.zhangyanwei.sct.service.dto.user.PureTokenUser;
import com.github.zhangyanwei.sct.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.github.zhangyanwei.sct.oauth2.server.vo.Me.from;
import static com.github.zhangyanwei.sct.service.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAnonymous()")
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public void create(@Valid @RequestBody PureTokenUser pureUser) throws ServiceException {
        userService.create(pureUser);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/me", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public Me me(@AuthenticationPrincipal UserDetails details) throws ServiceException {
        return from(details);
    }
}
