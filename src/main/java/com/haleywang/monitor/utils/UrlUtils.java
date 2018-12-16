package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlUtils {

    private static Logger LOG = LoggerFactory.getLogger(UrlUtils.class);

    public static final String UTF_8 = StandardCharsets.UTF_8.name();

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        return s;
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        return s;
    }

    public static String getDomainName(String url) throws URISyntaxException {
        if(url == null) {
            return null;
        }
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static String getPath(String url) {
        if(url == null) {
            return "";
        }
        try {
            return new URI(url).getPath();
        } catch (URISyntaxException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

}
