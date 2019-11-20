package com.forgqi.resourcebaseserver.client.parse;

import feign.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {
    public static final String[] COURSE_COLOR = {
            "#BFffa9d5", "#BFb896e7", "#BFffa941", "#BF38d3a9", "#BFffd440",
            "#BF64baff", "#BFff8693", "#BFffaeb9", "#BF00cdcd", "#BF00ced1",
            "#BF3cb371", "#BF6b8e23", "#BFfa8072",
    };
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

    public static List<String> OddEvenWeek(String odd_even_week_code){

        List<String> stringList = new ArrayList<>();


        String course_week[] = odd_even_week_code.split(",");
        for (String StringCourseWeek : course_week) {

            if (StringCourseWeek.contains("-")) {
                int min = Integer.parseInt(getNum(StringCourseWeek.split("-")[0]));
                int max = Integer.parseInt(getNum(StringCourseWeek.split("-")[1]));

                for (int j = min; j <= max; j++) {
                    if (StringCourseWeek.contains("单")) {
                        if (j % 2 == 1)
                            stringList.add(getNum(j + ""));
                    }else if (StringCourseWeek.contains("双")) {
                        if (j % 2 == 0)
                            stringList.add(getNum(j + ""));
                    }else {
                        stringList.add(getNum(j + ""));

                    }
                }


            } else {
                stringList.add(getNum(StringCourseWeek));
            }

        }

        return stringList;

    }

    public static String getNum(String s){
        if (s !=null && s.length()>0) {

            Pattern p = Pattern.compile("[^0-9]");
            Matcher m = p.matcher(s);
            String result = m.replaceAll("");
            if (result.length() > 0) {
                return result;
            } else {
                return "";
            }
        }else {
            return "";
        }
    }
}
