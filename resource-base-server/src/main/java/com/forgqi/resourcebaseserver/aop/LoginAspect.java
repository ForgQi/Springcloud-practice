package com.forgqi.resourcebaseserver.aop;

import com.forgqi.resourcebaseserver.client.*;
import com.forgqi.resourcebaseserver.common.UserHelper;
import com.forgqi.resourcebaseserver.dto.LoginDTO;
import feign.Response;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.CookieManager;
import java.util.Optional;

// 如果在同一个 aspect 类中，针对同一个 pointcut，定义了两个相同的 advice(比如，定义了两个 @Before)，
// 在这种情况下，通知的执行顺序是按照方法名，从左往右依次逐字符比较的，
// 只要有一个位置的字符能够确定顺序（也就是不一样），那么就不会再往下比较了，
// 直接根据这个位置两个方法名中字符的ACSII码大小决定通知执行的顺序，ACSII码越小的通知越先执行。
@Aspect
@Component
public class LoginAspect {
    private final LoginFeignClient loginFeignClient;
    private final JwkFeignClient jwkFeignClient;
    private final GmsFeignClient gmsFeignClient;
    private final ZhxgFeignClient zhxgFeignClient;
    private final YjsxgFeignClient yjsxgFeignClient;
    private final CookieManager cookieManager;

    public LoginAspect(LoginFeignClient loginFeignClient, JwkFeignClient jwkFeignClient, GmsFeignClient gmsFeignClient, ZhxgFeignClient zhxgFeignClient, YjsxgFeignClient yjsxgFeignClient, CookieManager cookieManager) {
        this.loginFeignClient = loginFeignClient;
        this.jwkFeignClient = jwkFeignClient;
        this.gmsFeignClient = gmsFeignClient;
        this.zhxgFeignClient = zhxgFeignClient;
        this.yjsxgFeignClient = yjsxgFeignClient;
        this.cookieManager = cookieManager;
    }

    @Pointcut("within(com.forgqi.resourcebaseserver.service.client.JwkService)")
    public void jwkServicePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    @Before(value = "execution(* com.forgqi.resourcebaseserver.service.client.*.saveStuInfo(..)) && " +
//            "execution(* com.forgqi.resourcebaseserver.service.client.GmsService.saveStuInfo(..)) && " +
            "args(loginDTO)")
    public void loginFirst(LoginDTO loginDTO) throws IOException {
        cookieManager.getCookieStore().removeAll();
        Response response = loginFeignClient.post(UserHelper.getLoginMap(loginDTO.getUserName(), loginDTO.getPassword()));
        Optional.ofNullable(response.headers().get("Set-Cookie"))
                .orElseThrow(()-> new UsernameNotFoundException("用户名或密码错误！"));
//        Document document = ParseUtil.getDocument(html);不跳转暂时不启用
//        Element mailbox1 = document.getElementById("mailbox1");
//        loginDTO.setUserName(mailbox1.attr("value"));
    }
    @Before("within(com.forgqi.resourcebaseserver.service.client.*) && " +
            "!execution(* com.forgqi.resourcebaseserver.service.client.JwkService.saveStuInfo(..)) && " +
            "!execution(* com.forgqi.resourcebaseserver.service.client.GmsService.saveStuInfo(..))")
    public void login() {
        cookieManager.getCookieStore().removeAll();
        UserHelper.getUserLoginMap()
                .map(loginMap -> loginFeignClient.post(loginMap).headers().get("Set-Cookie"))
                .orElseThrow(()-> new UsernameNotFoundException("用户名或密码错误！"));
    }

    @Before("jwkServicePointcut()")
    public void loginJwk() {
        jwkFeignClient.login();
    }

    @Before("within(com.forgqi.resourcebaseserver.service.client.GmsService)")
    public void loginGms() {
        gmsFeignClient.login();
    }

    @Before("within(com.forgqi.resourcebaseserver.service.client.ZhxgService)")
    public void loginZhxg() {
        zhxgFeignClient.login();
    }

    @Before("within(com.forgqi.resourcebaseserver.service.client.YjsxgService)")
    public void loginYjsxg() {
        yjsxgFeignClient.login();
    }
}
