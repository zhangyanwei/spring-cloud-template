package com.github.zhangyanwei.sct.model.validation;

import javax.validation.groups.Default;

public interface Group {

    interface Save extends Default {}
    interface Update extends Default {}
    interface Remove extends Default {}

}
