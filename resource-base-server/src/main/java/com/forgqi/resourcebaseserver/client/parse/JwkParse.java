package com.forgqi.resourcebaseserver.client.parse;

import com.forgqi.resourcebaseserver.client.JwkFeignClient;
import com.forgqi.resourcebaseserver.dto.StuInfoDTO;
import feign.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwkParse {
    private final JwkFeignClient jwkFeignClient;

    public JwkParse(JwkFeignClient jwkFeignClient) {
        this.jwkFeignClient = jwkFeignClient;
    }

    public StuInfoDTO getStuInfo() throws IOException {
        Response response = jwkFeignClient.getStuInfo();

        Document document = ParseUtil.getDocument(response);

        Element element = document.getElementsByClass("form").first();
        StuInfoDTO stuInfoDTO = new StuInfoDTO();
        stuInfoDTO.setId(Long.valueOf(element.select("tr").get(0).select("td").get(0).text()));
        stuInfoDTO.setName(element.select("tr").get(0).select("td").get(1).text());
        stuInfoDTO.setCollege(element.select("tr").get(1).select("td").get(0).text());
        stuInfoDTO.setSubject(element.select("tr").get(1).select("td").get(1).text());
        stuInfoDTO.setEducation(element.select("tr").get(2).select("td").get(1).text());
        stuInfoDTO.setGrade(element.select("tr").get(3).select("td").get(0).text());
        stuInfoDTO.setClassNo(element.select("tr").get(3).select("td").get(1).text());
        stuInfoDTO.setIdCard(element.select("tr").get(4).select("td").get(1).text());
        return stuInfoDTO;
    }
}
