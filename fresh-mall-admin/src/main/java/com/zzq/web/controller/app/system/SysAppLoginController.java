package com.zzq.web.controller.app.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.domain.model.LoginUser;
import com.zzq.common.enums.UserLoginType;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.web.controller.app.system.service.ISysAppLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * app端登录
 * @version 1.0
 * @date 2023/9/1
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysAppLoginController extends BaseAppController {

    private final ISysAppLoginService appLoginService;


    /**
     * 账号密码登录
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/accountLogin")
    public R<Map<String, Object>> accountLogin(String username,String password) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = appLoginService.accountLogin(username,password);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);

    }

    /**
     * 短信验证码登录
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/phoneLogin")
    public R<UserVo> phoneLogin(String phone, String verifyCode) {
        // 生成令牌
        UserVo userVo = appLoginService.phoneLogin(phone,verifyCode);
        return R.ok(userVo);
    }

    /**
     * 账号密码注册
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/accountRegister")
    public R<Map<String, Object>> accountRegister(String username,String password) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = appLoginService.accountRegister(username,password);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);

    }


    /**
     * 账号密码修改
     * @return 结果
     */
    @GetMapping("/accountUpdate")
    public R<Boolean> accountUpdate(String oldPassword,String newPassword) {
        LoginUser loginUser = getAppLoginUser();
        return R.ok(appLoginService.accountUpdate(loginUser.getUsername(),oldPassword,newPassword));
    }

    /**
     * 公众号登录
     *
     * @param code 小程序code
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/h5Login")
    public R<Map<String, Object>> h5Login(@NotBlank(message = "{h5.code.not.blank}") String code) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = appLoginService.h5Login(code);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 小程序登录
     *
     * @param code 小程序code
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/miniLogin")
    public R<UserVo> miniLogin(Integer loginType,String code) {
        if (loginType.equals(UserLoginType.MP_WEIXIN.getCode())) {
            return R.ok(appLoginService.miniLogin(code));
        }
       return null;
    }

    /**
     * 小程序登录（第二步）
     *
     * @return 结果
     */
    @SaIgnore
    @GetMapping("/authPhone")
    public R<UserVo> authPhone(String encryptedData,
                                 String iv,
                                 Integer loginType,
                                 String session_key,
                                 String openId,
                                 String avatar,
                                 String nickName) {
        return R.ok(appLoginService.authPhone(encryptedData,iv,loginType,session_key,openId,avatar,nickName));
    }
}
