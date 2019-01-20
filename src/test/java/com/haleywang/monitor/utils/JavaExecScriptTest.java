package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;

public class JavaExecScriptTest {
	
	
	@Test
	public void testJsRunTestCode() throws Exception {
		
		String response = "{\"_meta\": {\"hint\": \"OK\", \"response_status\": \"200\" }}";
		String code = "var jsonData = JSON.parse($response); $tests[\"has key\"] = _.has(jsonData, \"_meta\") ;\n$tests[\"Your test name\"] = jsonData._meta.hint === \"OK\";";
		String res = JavaExecScript.jsRunTestCode(code, response, "{}");
		Assert.assertEquals("{\"has key\":true,\"Your test name\":true}", res);
	}

	@Test
	public void testJavaCallJsFunction() throws Exception {
		//code = code.replaceAll("[\\n\\r]", "");
		ScriptEngine se = JavaExecScript.se;
		String script = "function test(response){ var tests = {} ;try{ tests.ii = 0; } catch(e){tests.error = e.toString();} return JSON.stringify(tests); }";

		se.eval(script);

		Invocable inv2 = (Invocable) se;
		String res = (String) inv2.invokeFunction("test", "1");

		System.out.println(res);
	}


	@Test
	public void testJsCallJavaFunction() throws Exception {
		//code = code.replaceAll("[\\n\\r]", "");
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");

		Persion persion = new Persion();
		persion.setName("AA");
		persion.say("00");

		engine.put("persion",persion);
		engine.eval(" persion.say(\"123\"); ");

	}



    public static class Persion {
		private String name;

		public void setName(String name) {
			this.name = name;
		}

		public String say(String msg) {
			System.out.println(msg);
			return name + ":" + msg;
		}

	}

	@Test
	public void returnJson() throws IOException {

		String txt = FileTool.readInSamePkg(this.getClass(), "testjs.txt", true);

		Object o1 = JavaExecScript.returnJson(txt, "req");
		Assert.assertNotNull(o1);
		Assert.assertTrue(JsonUtils.toJson(o1).contains("<req>"));


		Object o = JavaExecScript.returnJson("var req = {a:{b:1}};", "req");
		Assert.assertEquals("{\"a\":{\"b\":1}}", JsonUtils.toJson(o));
	}

}
