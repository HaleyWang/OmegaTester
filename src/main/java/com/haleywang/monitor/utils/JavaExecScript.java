package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.haleywang.monitor.common.req.HttpTool;


//TODO use NashornScriptEngineFactory
public class JavaExecScript {


    /**
     * 运行JS对象中的函数
     *
     * @return
     */
    public static Object jsObjFunc() {
        String script = "var obj={run:function(){return 'run method : return:\"abc'+this.next('test')+'\"';},next:function(str){return ' 我来至next function '+str+')'}}";
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine scriptEngine = sem.getEngineByName("js");
        try {
            scriptEngine.eval(script);
            Object object = scriptEngine.get("obj");
            Invocable inv2 = (Invocable) scriptEngine;
            return (String) inv2.invokeMethod(object, "run");
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    /**
     * 获取js对象数字类型属性
     *
     * @return
     */
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

    /**
     * JS计算
     *
     * @param script
     * @return
     */
    public static Object jsCalculate(String script) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            return (Object) engine.eval(script);
        } catch (ScriptException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    private final static Pattern PATTERN_BACK_QUOTE = Pattern.compile("`[\\s\\n]*;");
    public static int lastIndexOfRegex(String str, Pattern pattern)
    {
        Matcher matcher = pattern.matcher(str);

        // Default to the NOT_FOUND constant
        int lastIndex = -1;

        // Search for the given pattern
        while (matcher.find())
        {
            lastIndex = matcher.start();
        }

        return lastIndex;
    }

    private final static Pattern PATTERN_VAR_NAME = Pattern.compile("var\\s+(\\w+)\\s+");

    public static Object returnJson(String txt , String jsonVariableName) {

        int lastIndex = txt.lastIndexOf("`;") + 2;

        String left = txt.substring(0, lastIndex);

        String right = txt.substring(lastIndex);

        left =  left.replaceAll("=\\s*`", "='");
        left =  left.replaceAll("`[\\s\\n]*;", "';");
        left = left.replaceAll("\\n", " ");

        String script = left + right;


        Matcher matcher = PATTERN_VAR_NAME.matcher(script);
        String varName = jsonVariableName;
        if(jsonVariableName == null && matcher.find()) {
            varName = matcher.group(1);
        }

        String functionScript = "function say() { " + script + " ; return "+ varName +"; }" ;

        //ScriptEngine se = new NashornScriptEngineFactory().getScriptEngine("--language=es6");


        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        try {
            se.eval(functionScript);
            Invocable inv2 = (Invocable) se;
            return inv2.invokeFunction("say", null);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }



    /**
     * 运行JS基本函数
     */
    public static void jsFunction1() {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        try {
            String script = "function say(name){ return 'hello,'+name; }";
            se.eval(script);
            Invocable inv2 = (Invocable) se;
            String res = (String) inv2.invokeFunction("say", "test");
            System.out.println(res);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    static String lib = "";
    static ScriptEngine se = null;
    static {
        String file = "/static/js/underscore-min.js";
        URL url = JavaExecScript.class.getResource(file);


        try {
            lib = FileUtils.readFileToString(new File(url.getPath()), "utf-8");
            ScriptEngineManager sem = new ScriptEngineManager();
            se = sem.getEngineByName("javascript");
            se.eval(lib);
            HttpTool ht = new HttpTool();
            se.put("$httpTool", ht);

        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }

    public static String jsRunTestCode(String code, String response, String preReqResultStr) {
        if(code == null) {
            return null;
        }

        if(StringUtils.isEmpty(preReqResultStr)) {
            preReqResultStr = "{}";
        }
        //se.put("TestUtils", TestUtils);
        try {
            //code = code.replaceAll("[\\n\\r]", "");
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
        if(code == null) {
            return null;
        }

        //se.put("TestUtils", TestUtils);
        try {
            //code = code.replaceAll("[\\n\\r]", "");
            String script = "function runPreRequestScript($envString){ var $preReqResult = {} ;try{ " + code + " } catch(e){$preReqResult.error = e.toString();} return JSON.stringify($preReqResult); }";

            se.eval(script);

            Invocable inv2 = (Invocable) se;
            return (String) inv2.invokeFunction("runPreRequestScript", envString);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }



}