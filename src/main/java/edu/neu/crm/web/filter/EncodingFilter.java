package edu.neu.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入编码设置过滤器");

        //设置request对象编码格式
        request.setCharacterEncoding("UTF-8");
        //设置response相应输出流编码格式
        response.setContentType("text/html;charset=UTF-8");

        //将请求放行
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

}
