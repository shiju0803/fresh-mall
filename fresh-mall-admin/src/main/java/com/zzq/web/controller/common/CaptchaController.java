package com.zzq.web.controller.common;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import com.zzq.common.constant.CacheConstants;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.domain.R;
import com.zzq.common.enums.CaptchaType;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.common.utils.reflect.ReflectUtils;
import com.zzq.common.utils.spring.SpringUtils;
import com.zzq.framework.config.properties.CaptchaProperties;
import com.zzq.sms.config.properties.SmsProperties;
import com.zzq.sms.entity.SmsResult;
import com.zzq.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码操作处理
 */
@SaIgnore
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {

    private final CaptchaProperties captchaProperties;
    private final SmsProperties smsProperties;
    private final ISysConfigService configService;

    /**
     * 短信验证码
     *
     * @param phonenumber 用户手机号
     */
    @GetMapping("/captchaSms")
    public R<String> smsCaptcha(@NotBlank(message = "{user.phonenumber.not.blank}") String phonenumber) {
        if (!smsProperties.getEnabled()) {
            return R.fail("当前系统没有开启短信功能！");
        }
        String key = CacheConstants.CAPTCHA_CODE_KEY + phonenumber;
//        String code = RandomUtil.randomNumbers(4);
        String code = "6666";
        RedisUtils.setCacheObject(key, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        // 验证码模板id 自行处理 (查数据库或写死均可)
        String templateId = "";
        Map<String, String> map = new HashMap<>(1);
        map.put("code" , code);
//        SmsTemplate smsTemplate = SpringUtils.getBean(SmsTemplate.class);
//        SmsResult result = smsTemplate.send(phonenumber, templateId, map);

        SmsResult result = SmsResult.builder().isSuccess(true).build();
        if (!result.isSuccess()) {
            log.error("验证码短信发送异常 => {}" , result);
            return R.fail(result.getMessage());
        }
        return R.ok(code);
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public R<Map<String, Object>> getCode() {
        Map<String, Object> ajax = new HashMap<>();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled" , captchaEnabled);
        if (!captchaEnabled) {
            return R.ok(ajax);
        }
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        ajax.put("uuid" , uuid);
        ajax.put("img" , captcha.getImageBase64());
        return R.ok(ajax);
    }
}
