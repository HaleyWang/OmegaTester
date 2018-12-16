package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.haleywang.monitor.utils.http.HttpTool;

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