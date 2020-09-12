package com.haleywang.monitor.common.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author haley
 * @date 2018/12/16
 */
public class CustomMessageConverter extends ClassicConverter {
    private static final int BUF_SIZE = 256;
    private static final int MAX_TEXT_LENGTH = 9000;
    public static final String SPACE = " ";


    @Override
    public String convert(ILoggingEvent event) {
        return doLayout(event);
    }

    public String doLayout(ILoggingEvent event) {
        if (AppContext.getRequestId() == null) {
            AppContext.setRequestId(UUID.randomUUID().toString());
        }

        StringBuilder sbuf = new StringBuilder(BUF_SIZE);

        //sbuf.setLength(0)
        sbuf.append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date())).append(SPACE);
        sbuf.append("level=").append("\"").append(event.getLevel()).append("\"").append(SPACE);
        sbuf.append("logid=").append("\"").append(AppContext.getRequestId()).append("\"").append(SPACE);
        sbuf.append("msg=").append("\"").append(formatTextMsg(event.getFormattedMessage())).append("\"").append(SPACE);
        sbuf.append("service_name=").append("\"").append(ServerName.getLocalServerName()).append("\"").append(SPACE);


        return sbuf.toString();
    }



    private String formatTextMsg(String inMsg) {
        String outMsg = "";
        if (inMsg != null) {
            outMsg = inMsg.replace("\"", "'").replace("\n", "");
            outMsg = outMsg.substring(0, (outMsg.length() <= MAX_TEXT_LENGTH) ? outMsg.length() : MAX_TEXT_LENGTH);
        }
        return outMsg;
    }
}