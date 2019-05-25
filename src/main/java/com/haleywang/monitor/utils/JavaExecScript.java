package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.req.HttpTool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//TODO use NashornScriptEngineFactory

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

    static String lib = "";
    static ScriptEngine se = null;

    static {
        String filePath = PathUtils.getRoot() + "/static/js/underscore-min.js";
        filePath = filePath.replaceAll("//", "/").replaceAll("test-classes", "classes");

        try {
            lib = FileUtils.readFileToString(new File(filePath), "utf-8");
            ScriptEngineManager sem = new ScriptEngineManager();
            se = sem.getEngineByName(JAVASCRIPT);
            se.eval(lib);
            HttpTool ht = new HttpTool();
            se.put("$httpTool", ht);

        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    public static String jsRunTestCode(String code, String response, String preReqResultStr) {
        if (code == null) {
            return null;
        }

        if (StringUtils.isEmpty(preReqResultStr)) {
            preReqResultStr = "{}";
        }

        try {

            String assertThatFun = " function $assertThat(msg, actual,expect, condition) { if(!condition) { condition = function(a, e) { return a === e; } } try{ $tests[msg] = condition(actual, expect); if(!$tests[msg]) {$tests[msg] = JSON.stringify(arguments) ;} }catch(e) {$tests[ msg] = $tests[ msg] + ' ' + JSON.stringify(arguments) + ' ' + e.toString();}; }; ";

            String script = " function test($response, $preReqResultStr){ var $preReqResult = JSON.parse($preReqResultStr); var $tests = {}; " + assertThatFun + " try{ " + code + " } catch(e){$tests.error = e.toString();} return JSON.stringify($tests); }";

            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction("test", response, preReqResultStr);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public static String jsRunPreRequestScriptCode(String code, String envString) {
        if (code == null) {
            return null;
        }

        try {
            String script = "function runPreRequestScript($envString){ var $preReqResult = {} ;try{ " + code + " } catch(e){$preReqResult.error = e.toString();} return JSON.stringify($preReqResult); }";

            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction("runPreRequestScript", envString);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }


}