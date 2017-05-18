package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

public interface AjaxAuthenticationHandler<T extends AjaxAuthenticationHandler> {

    String DEFAULT_AJAX_PARAM = "ajax";

    String GEO_LAT_PARAM = "lat";

    String GEO_LNG_PARAM = "lng";

    T ajaxParam(String name);

}
