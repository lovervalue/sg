package com.sofn.bigdata.constant;

import java.io.Serializable;

/**
 * 返回码
 */
public class StatusCode implements Serializable {

    public static final int OK = 200;//成功
    public static final int ERROR = 400;//失败
    public static final int LOGIN_ERROR = 402;//用户名或密码错误
    public static final int ACCESS_ERROR = 403;//权限不足
    public static final int REMOTE_ERROR = 404;//远程调用失败
    public static final int REP_ERROR = 405;//重复操作
}
