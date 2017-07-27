package com.cn.xuyhproject.filter;

import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

public class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator iterator = request_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			// System.out.println(me.getKey()+":");
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
				System.out.println(values[i]);
				values[i] = xssClean(values[i]);
			}
		}
		return request_map;
	}
	private String xssClean(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
        	Policy policy = Policy.getInstance("antisamy-myspace-1.4.4.xml");
        	//CleanResults cr = antiSamy.scan(dirtyInput, policyFilePath); 
            final CleanResults cr = antiSamy.scan(value, policy);
			//安全的HTML输出
            return cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
}
}
