package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.req.HttpTool;
import com.haleywang.monitor.common.req.HttpUtils;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.ReqSetting;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//TODO use NashornScriptEngineFactory

/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaExecScript {
    private  static final Pattern PATTERN_VAR_NAME = Pattern.compile("var\\s+(\\w+)\\s+");
    public static final String JAVASCRIPT = "javascript";


    public static Object[] getArray() {
        ScriptEngineManager sem = new ScriptEngineManager();
        String script = "var obj={array:['test',true,1,1.0,2.11111]}";

        ScriptEngine scriptEngine = sem.getEngineByName("js");
        try {
            scriptEngine.eval(script);
            Object object2 = scriptEngine.eval("obj.array");
            Class<?> clazz = object2.getClass();
            Field denseField = clazz.getDeclaredField("dense");
            denseField.setAccessible(true);
            return (Object[]) denseField.get(object2);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public static Object returnJson(String txt, String jsonVariableName) {

        int lastIndex = txt.lastIndexOf("`;") + 2;

        String left = txt.substring(0, lastIndex);

        String right = txt.substring(lastIndex);

        left = left.replaceAll("=\\s*`", "='");
        left = left.replaceAll("`[\\s\\n]*;", "';");
        left = left.replaceAll("\\r|\\n", " ");

        String script = left + right;


        Matcher matcher = PATTERN_VAR_NAME.matcher(script);
        String varName = jsonVariableName;
        if (jsonVariableName == null && matcher.find()) {
            varName = matcher.group(1);
        }

        String functionScript = "function say() { " + script + " ; return " + varName + "; }";

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName(JAVASCRIPT);
        try {
            se.eval(functionScript);
            Invocable inv2 = (Invocable) se;
            return inv2.invokeFunction("say", null);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    private static String TEST_SCRIPT = "";
    private static String PRE_SCRIPT = "";

    static {

        try {
            TEST_SCRIPT = PathUtils.getRresource("conf/js_script/test_script.js");
            PRE_SCRIPT = PathUtils.getRresource("conf/js_script/pre_script.js");

        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    private static ScriptEngine createScriptEngine(String code) throws IOException, ScriptException {
        File jsLibFolder = new File(getJsLibFolder());
        FileUtils.ensureDirectoryExists(jsLibFolder);

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName(JAVASCRIPT);

        addLibDefault(se);
        addLibByCode(se, code);

        HttpTool ht = new HttpTool();
        se.put("$httpTool", ht);
        return se;
    }

    private static String getJsLibFolder() {
        return PathUtils.getRoot() + "/data1/js_lib";
    }

    private static void addLibDefault(ScriptEngine se) throws IOException, ScriptException {
        String fileRelatePath = "/static/js/underscore-min.js";
        if (!LIB_CACHE.containsKey(fileRelatePath)) {
            String lib = PathUtils.getRresource(fileRelatePath);
            LIB_CACHE.put(fileRelatePath, lib);
        }
        se.eval(LIB_CACHE.get(fileRelatePath));
    }

    static void addLibByCode(ScriptEngine se, String code) throws IOException, ScriptException {
        if (StringUtils.isBlank(code)) {
            return;
        }
        List<String> libUrls = parseLibUrls(code);
        for (String libUrl : libUrls) {
            addLibByUrl(libUrl, se);
        }
    }

    static final Pattern IMPORT_PATTERN = Pattern.compile("//\\s?import\\s+\"(.+)\"");

    static List<String> parseLibUrls(String code) {
        List<String> res = new ArrayList<>();
        String[] arr = code.split("\n");
        for (String line : arr) {
            line = line.trim();
            Matcher m = IMPORT_PATTERN.matcher(line);
            if (m.find()) {
                String url = m.group(1);
                res.add(url);
            }
        }
        return res;
    }

    static final Map<String, String> LIB_CACHE = new ConcurrentHashMap<>();

    static void addLibByUrl(String libUrl, ScriptEngine se) throws IOException, ScriptException {
        String lib = getCodeByLibUrl(libUrl);
        se.eval(lib);
    }

    static String getCodeByLibUrl(String libUrl) throws IOException {
        String fileRelatePath = PathUtils.urlToPath(libUrl);

        if (!LIB_CACHE.containsKey(fileRelatePath)) {

            File jsFile = new File(getJsLibFolder() + "/" + fileRelatePath);
            String lib;

            if (jsFile.exists()) {
                lib = org.apache.commons.io.FileUtils.readFileToString(jsFile, StandardCharsets.UTF_8);
            } else {
                UnirestRes result = HttpUtils.get(libUrl);
                lib = result.getBody();
                FileUtils.ensureDirectoryExists(jsFile.getParentFile());
                org.apache.commons.io.FileUtils.writeStringToFile(jsFile, lib, StandardCharsets.UTF_8);
            }

            LIB_CACHE.put(fileRelatePath, lib);
        }

        return LIB_CACHE.get(fileRelatePath);
    }

    public static String jsRunTestCode(String code, String response, String preReqResultStr, ReqSetting envString) {
        if (code == null) {
            return null;
        }

        if (StringUtils.isEmpty(preReqResultStr)) {
            preReqResultStr = "{}";
        }

        try {

            String script = TEST_SCRIPT.replace("{{my_code}}", code);

            ScriptEngine se = createScriptEngine(code);
            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction("test", response, preReqResultStr, envString);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public static String jsRunPreRequestScriptCode(String code, String envString) {
        if (code == null) {
            return null;
        }

        try {

            String script = PRE_SCRIPT.replace("{{my_code}}", code);

            ScriptEngine se = createScriptEngine(code);
            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction("runPreRequestScript", envString);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public static String jsRunScriptCode(String code, String arg) {
        if (code == null) {
            return null;
        }
        String script = code.trim();
        String funName = script.substring("function".length(), script.indexOf('(')).trim();

        try {
            ScriptEngine se = createScriptEngine(code);
            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction(funName, arg);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }


}