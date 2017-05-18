package com.github.zhangyanwei.sct.service.impl.access;

import com.github.zhangyanwei.sct.dao.repository.UserRepository;
import com.github.zhangyanwei.sct.service.dto.user.PureTokenUser;
import com.github.zhangyanwei.sct.service.dto.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.github.zhangyanwei.sct.model.entity.User.UserStatus.ACTIVE;
import static com.github.zhangyanwei.sct.model.entity.User.UserStatus.LOCKED;

@Component
public class UserAccess {

    @Autowired
    private UserRepository userRepository;

    public void create(PureTokenUser pureUser) {
        userRepository.save(pureUser.toUser());
    }

    public Optional<User> tryFind(Long userId) {
        return userRepository.findById(userId).map(User::from);
    }

    public Optional<User> tryFindByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone).map(User::from);
    }

    public void lock(Long[] userIds) {
        userRepository.updateStatus(LOCKED, userIds);
    }

    public void unlock(Long[] userIds) {
        userRepository.updateStatus(ACTIVE, userIds);
    }

    public void updateLastAccessTime(Long userId, Date time) {
        userRepository.findById(userId).ifPresent(user -> user.setLastAccessTime(time));
    }
}
