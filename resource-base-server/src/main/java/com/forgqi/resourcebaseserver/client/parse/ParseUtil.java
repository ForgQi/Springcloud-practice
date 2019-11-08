package com.forgqi.resourcebaseserver.client.parse;

import feign.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParseUtil {
    private static final Map<Character, Integer> numMap = new HashMap<>();
    static {
        numMap.put('一', 1);
        numMap.put('二', 2);
        numMap.put('三', 3);
        numMap.put('四', 4);
        numMap.put('五', 5);
        numMap.put('六', 6);
        numMap.put('日', 7);
    }
    public static Document getDocument(Response response) throws IOException {
        return Jsoup.parse(new String(response.body().asInputStream().readAllBytes(),
                response.headers().get("content-type").stream().findFirst().orElse("charset=UTF-8").split("=")[1]));
    }

    static int paresZh(char zh){
        return numMap.get(zh);
    }
}
