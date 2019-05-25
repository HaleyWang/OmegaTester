package com.haleywang.monitor.common.req;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.apache.commons.lang3.StringUtils;

public class PostmanDataUtils {

	private PostmanDataUtils(){}
	
	public static HttpRequestItem parsePostmanData(String aa)  {
		return parsePostManData(Arrays.asList(aa.split("\n")));
	}
	
	public static HttpRequestItem parsePostmanData(File file) throws IOException {
		List<String> lines = IOUtils.readLines(new FileInputStream(file), "UTF-8");
		return parsePostManData(lines);
	}
	

	public static HttpRequestItem parsePostManData(List<String> lines) {
		HttpRequestItem reqItem = new HttpRequestItem();
		int blankIdx = Integer.MAX_VALUE;
		for(int i = 0, n = lines.size(); i < n; i++) {
			String str = lines.get(i);
			if(str.trim().length() == 0) {
				blankIdx = i;
				continue;
			}
			if(i > blankIdx) {
				reqItem.appendData(str);
				continue;
			}
			str = str.trim();
			str = str.replace("[\\s]+", " ");
			String[] arr = str.split(" ");
			if(arr.length < 2) {
				continue;
			}
			if(i == 0) {
				reqItem.setHttpMethod(HttpMethod.valueOf(StringUtils.upperCase(arr[0])));
				reqItem.setPath(arr[1]);
				continue;
			}
			if(i == 1) {
				reqItem.setHost(arr[1]);
			}
			
			reqItem.addReqHeader(arr[0].replaceAll(":", ""), arr[1]);
		}
		return reqItem;
	}
	

}
