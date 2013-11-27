package com.cmbc.config;

import java.util.EnumSet;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.cmbc.web.MdcFilter;
import ch.rasc.edsutil.optimizer.WebResourceProcessor;

/**
 * 入手点，该类最终实现 WebApplicationInitializer接口，等同于配置web.xml同样的功效,该接口是spring定义的接口。
 * SpringServletContainerInitializer负责调用起所有实现WebApplicationInitializer接口的类，
 * SpringServletContainerInitializer实现了javax.servlet.ServletContainerInitializer,
 * 接口ServletContainerInitializer和注解HandleType是 Servlet 3.0中的新功能，用于在启动阶段注册servlet,listener等
 *
 * 如果有多个的话，如何控制执行的顺序。
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		String profile = System.getProperty("spring.profiles.active");
		if (profile == null) {
			try {
				profile = (String) JndiLocatorDelegate.createDefaultResourceRefLocator().lookup(
						"spring.profiles.active");
			} catch (NameNotFoundException ne) {
				// do nothing
			} catch (NamingException e) {
				throw new ServletException(e);
			}
		}

        //如果是生产模式的话，根据 [webresources.txt]进行js代码的优化，合并和压缩等操作
		WebResourceProcessor processor = new WebResourceProcessor(!"development".equals(profile));
		processor.process(servletContext);

		servletContext.setInitParameter("spring.profiles.default", "production");

        //----------------------- 注册【CharacterEncodingFilter】filter ------------------------------
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(false);
		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter).addMappingForUrlPatterns(null,
				false, "/*");

        //----------------------- 注册【MdcFilter】filter ------------------------------
		servletContext.addFilter("mdcFilter", MdcFilter.class).addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");

		super.onStartup(servletContext);
	}

    /**
     *  向applicationContext对象中增加bean定义，applicationContext.xml?
     * @return
     */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { ComponentConfig.class, DataConfig.class, ScheduleConfig.class, SecurityConfig.class,
				WebConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

    /**
     * 创建ApplicationContext对象，
     * GenericWebApplicationContext ，it is designed for programmatic setup
     * @return
     */
	@Override
	protected WebApplicationContext createServletApplicationContext() {
		return new GenericWebApplicationContext();
	}

    /**
     *  Specify the servlet mapping(s) for the DispatcherServlet
     * @return
     */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}