package com.forgqi.resourcebaseserver.service.dto.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class RichTextHelper {
    private final String html;

    public RichTextHelper(String html) {
        this.html = html;
    }

    public String parseSummary() {
        String s = Jsoup.clean(html, Whitelist.basic());
        return s.length() > 100 ? s.substring(0, 99) : s;
    }
}
