package com.cmbc.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *  NDC（Nested Diagnostic Context）和MDC（Mapped Diagnostic Context）是log4j种非常有用的两个类，它们用于存储应用程序的上下文信息（context infomation），
 *  从而便于在log中使用这些上下文信息。
 *  NDC的实现是用hashtable来存储每个线程的stack信息，这个stack是每个线程可以设置当前线程的request的相关信息，
 *  MDC的实现是使用threadlocal来保存每个线程的Hashtable的类似map的信息，
 */
public class MdcFilter implements Filter {

	@Override
	public void init(FilterConfig config) {
		// no action
	}

	@Override
	public void destroy() {
		// no action
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			MDC.put("userName", authentication.getName());
		}

		MDC.put("ip", request.getRemoteAddr());
		MDC.put("userAgent", ((HttpServletRequest) request).getHeader("User-Agent"));

		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove("userName");
			MDC.remove("ip");
			MDC.remove("userAgent");
		}
	}
}