package com.dc18TokenExchange.Resourceserver;

import org.springframework.stereotype.Service;

@Service
public class StringRequestParser {
    public Long getLongValueFromString(String var, String splicer) {
        var = var.substring(splicer.length());
        return Long.parseLong(var);
    }
}
