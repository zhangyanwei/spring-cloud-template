package com.github.zhangyanwei.sct.service.dto.user;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

import static com.github.zhangyanwei.sct.common.utils.RegexPattern.REGEX_PHONE;

public class PureUser extends User {

    private static final long serialVersionUID = 4360845789685360831L;

    @Pattern(regexp = REGEX_PHONE)
    @NotBlank
    public String getMobile() {
        return super.getMobile();
    }

}
