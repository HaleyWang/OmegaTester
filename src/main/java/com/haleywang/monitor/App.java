package com.haleywang.monitor;

import com.haleywang.monitor.common.mvc.Server;
import com.haleywang.monitor.common.req.AnnoManageUtil;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.ctrl.v1.ConfigCtrl;
import com.haleywang.monitor.dto.VersionObj;
import com.haleywang.monitor.schedule.CronScheduleHelper;
import com.haleywang.monitor.utils.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class App {

    public static void main( String[] args) throws Exception {
        Server.start(args);
        CronScheduleHelper.start();

    }


}
