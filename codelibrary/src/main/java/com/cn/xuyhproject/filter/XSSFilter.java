package com.cn.xuyhproject.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: XUYH
 * @Description: XSS过滤器，参考：http://www.cnblogs.com/wangdaijun/p/5652864.html
 * @Date: 2017/7/27
 * @Version:
 */

public class XSSFilter implements Filter{

    private FilterConfig filterConfig;
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new RequestWrapper((HttpServletRequest) request), response);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        this.filterConfig = arg0;
    }
}
