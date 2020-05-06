package com.forgqi.resourcebaseserver.service.dto.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RichTextHelper {
    private Document document;

    public RichTextHelper(String html) {
        document = Jsoup.parse(html);
    }

    public String parseSummary() {
        String s = document.text();
        return s.length() > 100 ? s.substring(0, 99) : s;
    }
}
