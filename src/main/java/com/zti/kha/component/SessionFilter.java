package com.zti.kha.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Session filter
 * @author
 *
 */
@Component
@Order(2)
public class SessionFilter implements Filter {

    static Logger log = LogManager.getLogger(SessionFilter.class);


    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
//        log.info("Initializing filter :{}", this);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String appContext = req.getContextPath() + "/api";
        //========================//
        // Skip uri pattern 'API'
        //========================//
        if (uri.startsWith(appContext)) {
            // do nothing
        }
        //========================//
        // Check session
        //========================//
//        else{
//            HttpSession session = ((HttpServletRequest) request).getSession();
//            Object userSession = session.getAttribute(WebMessage.USER_SESSION);
//            if(userSession == null){
//                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
//                return;
//            }
//        }

        chain.doFilter(request, response);

        String ipAddress = req.getRemoteAddr();
//        log.info("Logging Request  {} : {} : {}", req.getMethod(), req.getRequestURI(), req.getRequestURL() + "\t" + ipAddress);

//        if (isValidAccount(ipAddress)) {
//
//        }
    }


    @Override
    public void destroy() {
//        log.warn("Destroy filter :{}", this);
    }

}