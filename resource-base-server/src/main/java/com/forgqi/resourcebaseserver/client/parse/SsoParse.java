package com.forgqi.resourcebaseserver.client.parse;

import com.forgqi.resourcebaseserver.client.LoginFeignClient;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import feign.Response;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SsoParse {
    private final LoginFeignClient loginFeignClient;

    public SsoParse(LoginFeignClient loginFeignClient) {
        this.loginFeignClient = loginFeignClient;
    }

    public Map<String, String> getLoginMap() {
        Response response = loginFeignClient.get();
        Document document = ParseUtil.getDocument(response).orElseThrow();
        if (!(document.getElementById("captcha") == null)) {
            throw new OperationException("需要输入验证码，请稍后再试");
        }
        String lt = document.getElementsByAttributeValue("name", "lt").first().val();
        String execution = document.getElementsByAttributeValue("name", "execution").first().val();
        Map<String, String> map = new HashMap<>();
        map.put("lt", lt);
        map.put("execution", execution);
        map.put("_eventId", "submit");
        map.put("btn", "");
        return map;
    }
}
