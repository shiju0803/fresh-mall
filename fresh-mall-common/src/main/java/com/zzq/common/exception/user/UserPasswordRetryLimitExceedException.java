package com.zzq.common.exception.user;

/**
 * 用户错误最大次数异常类
 */
public class UserPasswordRetryLimitExceedException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime) {
        super("user.password.retry.limit.exceed", retryLimitCount, lockTime);
    }

}
