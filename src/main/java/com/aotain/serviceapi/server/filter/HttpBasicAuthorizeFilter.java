package com.aotain.serviceapi.server.filter;


import com.aotain.common.config.ContextUtil;
import com.aotain.cu.auth.ResponseCode;
import com.aotain.cu.auth.ResponseData;
import com.aotain.serviceapi.server.config.PropertyConfigBean;
import com.aotain.serviceapi.server.util.JWTUtils;
import com.aotain.serviceapi.server.util.JsonUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/11/20
 */
public class HttpBasicAuthorizeFilter implements Filter {
    JWTUtils jwtUtils = JWTUtils.getInstance();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        String auth = httpRequest.getHeader("Authorization");

        PropertyConfigBean propertyConfigBean = ContextUtil.getContext().getBean(PropertyConfigBean.class);
        String uri = httpRequest.getRequestURI().replace(propertyConfigBean.getContextPath(),"");
        // 获取token请求不拦截
        if (uri.equals("/oauth/token")){
            chain.doFilter(request,response);
            return;
        }
        //健康检查控制
        if (uri.equals("/autoconfig") || uri.equals("/configprops") || uri.equals("/beans") || uri.equals("/dump")
                || uri.equals("/env") || uri.equals("/health") || uri.equals("/info") || uri.equals("/mappings")
                || uri.equals("/metrics") || uri.equals("/shutdown") || uri.equals("/trace")) {
            if(httpRequest.getQueryString() == null || !httpRequest.getQueryString().equals("token=admin123456")){
                PrintWriter print = httpResponse.getWriter();
                print.write(JsonUtils.toJson(ResponseData.fail("非法请求【缺少token信息】", ResponseCode.NO_AUTH_CODE.getCode())));
                return;
            }
            chain.doFilter(request, response);
        } else {
            //验证TOKEN
            if (!StringUtils.hasText(auth)) {
                PrintWriter print = httpResponse.getWriter();
                print.write(JsonUtils.toJson(ResponseData.fail("非法请求【缺少Authorization信息】", ResponseCode.NO_AUTH_CODE.getCode())));
                return;
            }
            JWTUtils.JWTResult jwt = jwtUtils.checkToken(auth);
            if (!jwt.isStatus()) {
                PrintWriter print = httpResponse.getWriter();
                print.write(JsonUtils.toJson(ResponseData.fail(jwt.getMsg(), jwt.getCode())));
                return;
            }

//            if (!"auth-aaab".equals(auth)) {
//                PrintWriter print = httpResponse.getWriter();
//                print.write(JsonUtils.toJson(ResponseData.fail("非法请求【Authorization信息不正确】", ResponseCode.NO_AUTH_CODE.getCode())));
//                return;
//            }
            chain.doFilter(httpRequest, response);
        }

    }

    @Override
    public void destroy() {

    }
}
