package com.zzq.web.controller.app.system.service;

import com.zzq.user.domain.vo.UserVo;

public interface ISysAppLoginService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    UserVo miniLogin(String code);

    /**
     * h5登录
     * @param openid
     * @return
     */
    String h5Login(String openid);

    /**
     * 账号密码登录
     * @param username
     * @param password
     * @return
     */
    String accountLogin(String username, String password);

    /**
     * 账号密码注册
     * @param username
     * @param password
     * @return
     */
    String accountRegister(String username, String password);

    /**
     * 密码修改
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Boolean accountUpdate(String username, String oldPassword, String newPassword);

    /**
     * 手机验证码登录
     * @param phone
     * @param verifyCode
     * @return
     */
    UserVo phoneLogin(String phone, String verifyCode);

    /**
     * 登录
     * @param encryptedData
     * @param iv
     * @param loginType
     * @param session_key
     * @param openId
     * @param avatar
     * @param nickName
     * @return
     */
    UserVo authPhone(String encryptedData,
                       String iv,
                       Integer loginType,
                       String session_key,
                       String openId,
                       String avatar,
                       String nickName);
}
