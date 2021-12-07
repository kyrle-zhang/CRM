package edu.neu.crm.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getServletPath();
        //登陆相关的资源不需要验证
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            filterChain.doFilter(req,resp);

        //其他资源全部需要验证
        }else{
            //验证用户是否进行过登陆操作
            if(request.getSession().getAttribute("user") == null){
                //重定向至登陆页面
                //重定向的路径必须是: /项目名/资源名
                //请求转发的路径必须是: /资源名
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }else {
                //用户已经登陆过，正常放行
                filterChain.doFilter(req,resp);
            }
        }


    }

    @Override
    public void destroy() {

    }
}
