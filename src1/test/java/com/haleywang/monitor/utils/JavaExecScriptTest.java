package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;
import org.junit.Assert;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.List;

public class JavaExecScriptTest {

	@Test
	public void jsRunPreRequestScriptCode() throws Exception {

		String envStr = "{\"env\":\"QA\"}";
		String code = "$preReqResult.env = $env.env;$preReqResult.other = 1";
		String res = JavaExecScript.jsRunPreRequestScriptCode(code, envStr);
		Assert.assertEquals("{\"env\":\"QA\",\"other\":1,\"$log\":[]}", res);
	}

	@Test
	public void testJsRunTestCode() throws Exception {

		String response = "{\"_meta\": {\"hint\": \"OK\", \"response_status\": \"200\" }}";
		String code = "var jsonData = JSON.parse($response); $tests[\"has key\"] = _.has(jsonData, \"_meta\") ;\n$tests[\"Your test name\"] = jsonData._meta.hint === \"OK\";";
		String res = JavaExecScript.jsRunTestCode(code, response, "{}", null);
		Assert.assertEquals("{\"has key\":true,\"Your test name\":true,\"$env\":null,\"$log\":[]}", res);
	}


	@Test
	public void testJsCallJavaFunction() throws Exception {
		//code = code.replaceAll("[\\n\\r]", "");
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");

		Person person = new Person();
		person.setName("AA");
		person.say("00");

		engine.put("person", person);
		engine.eval(" person.say(\"123\"); ");

	}

    @Test
    public void jsRunScriptCode() {

		String code = "function out(arg) { return 'You input is:' + arg; }";
		String arg = "hello";
		String out = JavaExecScript.jsRunScriptCode(code, arg);
		Assert.assertEquals("You input is:hello", out);
    }


    public static class Person {
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
	public void getCodeByLibUrl() throws IOException {
		String url = "https://cdn.jsdelivr.net/npm/base-64@1.0.0/base64.min.js";

		String res = JavaExecScript.getCodeByLibUrl(url);

		Assert.assertTrue(res.contains("base64 v1.0.0"));
	}

	@Test
	public void parseLibUrls() throws IOException {
		String code = PathUtils.getRresource("conf/js_script/lib_url.js");

		List<String> res = JavaExecScript.parseLibUrls(code);
		Assert.assertEquals(2, res.size());
		Assert.assertEquals("https://cdn.jsdelivr.net/npm/md5.js@1.3.5/index.js", res.get(0));
		System.out.println(res);
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

	@Test
	public void jsRunScriptCode1() {

		String code = "function out(arg) { return 'You input is:' + arg; }";
		String arg = "hello";

		String out = jsRunScriptCode(code, arg);
		Assert.assertEquals("You input is:hello", out);
	}


	public static String jsRunScriptCode(String code, String arg) {
		if (code == null) {
			return null;
		}
		String script = code.trim();
		String funName = script.substring("function".length(), script.indexOf('(')).trim();

		try {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine se = sem.getEngineByName("javascript");

			String lib = PathUtils.getRresource("/static/js/underscore-min.js");
			se.eval(lib);
			se.eval(lib);

			se.eval(script);

			Invocable inv2 = (Invocable) se;
			return (String) inv2.invokeFunction(funName, arg);
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);

		}
	}

}
