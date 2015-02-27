package com.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class EncodingFilterDispatcher extends StrutsPrepareAndExecuteFilter  {
	
	  private static String encoding = "UTF-8";    
	  
	      public void init(FilterConfig filterConfig) throws ServletException { 
	           super.init(filterConfig);    
	            String encodingParam = filterConfig.getInitParameter("encoding"); //获取web.xml下的初始化参数   
	            if (encodingParam != null && encodingParam.trim().length() != 0) {    
	                encoding = encodingParam;    
	          }    
	        }    
	   
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {    
	             request.setCharacterEncoding(encoding); //设置request编码格式   
	             response.setCharacterEncoding(encoding);//设置response编码格式   
	             super.doFilter(request, response, chain);    
	 }    

}
