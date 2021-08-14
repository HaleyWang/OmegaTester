package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2018/12/8.
 */
public class JSModifiedJavaTest {


    public void sayHello(String name){
        System.out.println("*************Hello***************"+name);
    }

    private static String getScript() {
        String script =
                "var index; " +
                        "var usname = usList.toArray(); " +
                        "usList.add(\"JLee 3\"); " +
                        "usList.add(\"JLee 4\"); "  +
                        "obj.sayHello('JLee')" ;
        return script;
    }


    @Test
    public void testSayHello() throws Exception {

        List<String> us = new ArrayList<String>();
        us.add("JLee 1");
        us.add("JLee 2");

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("js");

        engine.put("usList", us);
        engine.put("obj", new JSModifiedJavaTest()) ;

        try {
            engine.eval(getScript());
            for (String usname : us) {
                System.out.println("u: = " + usname);
            }
        } catch (ScriptException e) {
            throw new ReqException(e.getMessage(), e);
        }

        ScriptEngine engine2 = manager.getEngineByName("JavaScript");
        try {
            engine2.eval("print('Hello JLee ...')");
        } catch (ScriptException e) {
            throw new ReqException(e.getMessage(), e);
        }

    }

}