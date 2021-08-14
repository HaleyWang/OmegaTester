package com.haleywang.demo;

import java.util.List;
import java.util.regex.*;

import com.haleywang.monitor.utils.JsonUtils;
import lombok.Data;

public class ReqDemo {

    public static void main(String[] args) {


        final String regex = "(a*)*b";
        final String string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        System.out.println(string.length());

        final Pattern pattern = Pattern.compile(regex);

        long t = System.currentTimeMillis();

        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
        System.out.println((System.currentTimeMillis() -t)/1000);

    }

    public static void main2(String[] args) {
        String badRegex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";

        String bugUrl = "http://www.fapiao.com/dddp-web/pdf/download?request=6e7JGxxxxx4ILd-kExxxxxxxqJ4-CHLmqVnenXC692m74H38sdfdsazxcUmfcOH2fAfY1Vw__%5EDadIfJgiEf";
        if (bugUrl.matches(badRegex)) {
            System.out.println("match!!");
        } else {
            System.out.println("no match!!");
        }
    }




}


