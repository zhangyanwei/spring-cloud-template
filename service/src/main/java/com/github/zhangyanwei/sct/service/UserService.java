package com.github.zhangyanwei.sct.service;

import com.github.zhangyanwei.sct.service.dto.user.PureTokenUser;
import com.github.zhangyanwei.sct.service.dto.user.User;
import com.github.zhangyanwei.sct.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Optional;

import static com.github.zhangyanwei.sct.service.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/users")
public interface UserService {

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    void create(@RequestBody PureTokenUser pureUser) throws ServiceException;

    @Nonnull
    @RequestMapping(value = "/by_id", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<User> tryFind(@RequestParam("id") Long userId);

    @Nonnull
    @RequestMapping(value = "/by_phone", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<User> tryFindByPhone(@RequestParam("phone") String phone);

    @RequestMapping(value = "/lock", method = POST)
    void lock(@RequestBody Long[] userIds);

    @RequestMapping(value = "/lock", method = DELETE)
    void unlock(@RequestHeader("X-IDs") Long[] userIds);

    @RequestMapping(value = "/last_access_time", method = PUT)
    void updateLastAccessTime(@RequestParam("userId") Long userId, @RequestBody Date time) throws ServiceException;
}