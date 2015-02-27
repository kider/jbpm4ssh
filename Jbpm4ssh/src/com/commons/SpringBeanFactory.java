package com.commons;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanFactory {
	
	private static ApplicationContext springContext;
	private static final String[] contextPath = {
			"com/config/spring_system/spring_hibernate_jbpm.xml",
			"com/config/spring_*/spring_*.xml",
			"com/config/spring_*/action_*.xml"};
	public static ApplicationContext getSpringContext() {
		if (springContext == null) {
			springContext = new ClassPathXmlApplicationContext(contextPath);
		}
		return springContext;
	}

	public static Object getBean(String beanName) {
		return getSpringContext().getBean(beanName);
	}


}
