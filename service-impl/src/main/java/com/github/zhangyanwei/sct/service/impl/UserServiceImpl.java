package com.github.zhangyanwei.sct.service.impl;

import com.github.zhangyanwei.sct.service.UserService;
import com.github.zhangyanwei.sct.service.dto.user.PureTokenUser;
import com.github.zhangyanwei.sct.service.dto.user.User;
import com.github.zhangyanwei.sct.service.exception.ServiceException;
import com.github.zhangyanwei.sct.service.impl.access.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Service
@RestController
@Transactional
public class UserServiceImpl implements UserService {

    private final UserAccess userAccess;

    @Autowired
    public UserServiceImpl(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    @Override
    public void create(@Valid @RequestBody PureTokenUser pureUser) throws ServiceException {
        pureUser.setAuthorities(null);
        userAccess.create(pureUser);
    }

    @Nonnull
    @Override
    public Optional<User> tryFind(@RequestParam("id") Long userId) {
        return userAccess.tryFind(userId);
    }

    @Nonnull
    @Override
    public Optional<User> tryFindByPhone(@RequestParam("phone") String phone) {
        return userAccess.tryFindByPhone(phone);
    }

    @Override
    public void lock(@RequestBody Long[] userIds) {
        userAccess.lock(userIds);
    }

    @Override
    public void unlock(@RequestHeader("X-IDs") Long[] userIds) {
        userAccess.unlock(userIds);
    }

    @Override
    public void updateLastAccessTime(@RequestParam("userId") Long userId, @RequestBody Date time) throws ServiceException {
        userAccess.updateLastAccessTime(userId, time);
    }
}