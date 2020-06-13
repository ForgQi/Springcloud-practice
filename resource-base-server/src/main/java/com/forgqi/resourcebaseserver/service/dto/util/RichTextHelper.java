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
        return parseSummary(100);
    }

    public String parseSummary(int length) {
        String s = Jsoup.clean(html, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false));
        return s.length() > length ? s.substring(0, length - 1) : s;
    }
}
