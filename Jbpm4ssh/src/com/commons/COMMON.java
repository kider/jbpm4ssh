package com.commons;

/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2010-5-6
 *******************************************************************************/

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

public class COMMON {
	
    public static String DB_DIALECT = "ORACLE";
    private static String uid = "0";
    
	/**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DATA_FORMAT = "yyyy-MM-dd";

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
    /**
     * 时间格式：yyyyMMddHHmmss
     */
    public static final String TIME_FORMAT_TWO="yyyyMMddHHmmss";
    
    /**
     * 数据库操作动作：创建
     */
    public static int OPERATE_CREATE = 1;

    /**
     * 数据库操作动作：删除
     */
    public static int OPERATE_REMOVE = 2;

    /**
     * 数据库操作动作：更新
     */
    public static int OPERATE_UPDATE = 3;
    
    public static BigDecimal getBigDecimal(Double a,Double b)
    {
    	 BigDecimal d1= new BigDecimal(a);
    		d1.setScale(2, BigDecimal.ROUND_HALF_UP);
    		BigDecimal d2= new BigDecimal(b);
    		d2.setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		BigDecimal sum = d1.subtract(d2);
    		sum.setScale(2, BigDecimal.ROUND_HALF_UP);
    		MathContext mc = new MathContext(2);
    		sum=sum.round(mc);
    	return sum;
    }
   
    
    /**
     * 判断某个对象是否为空 集合类、数组做特殊处理
     * 
     * @param obj
     * @return 如为空，返回true,否则false
     * @author yehailong
     */
    @SuppressWarnings("unchecked")
	public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        // 如果不为null，需要处理几种特殊对象类型
        if (obj instanceof String) {
        	obj = obj.toString().trim();
            return obj.equals("");
        } else if (obj instanceof Collection) {
            // 对象为集合
            Collection coll = (Collection) obj;
            return coll.size() == 0;
        } else if (obj instanceof Map) {
            // 对象为Map
            Map map = (Map) obj;
            return map.size() == 0;
        } else if (obj.getClass().isArray()) {
            // 对象为数组
            return Array.getLength(obj) == 0;
        } else {
            // 其他类型，只要不为null，即不为empty
            return false;
        }
    }
    
    /**
     * 集合转换成MAP
     * 
     * @param cl 集合
     * @param keyProp KEY属性名
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Map collection2map(Collection cl, String keyProp) {
        if (isEmpty(cl)) {
            return new HashMap();
        }
        Iterator it = cl.iterator();
        Map map = new HashMap();
        while (it.hasNext()) {
            Object value = it.next();
            if (value instanceof Map) {
                Map temp = (Map) value;
                try {
                    map.put(temp.get(keyProp), value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    map.put(PropertyUtils.getProperty(value, keyProp), value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
	public static String getQuerySql(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer();
        Enumeration params = request.getParameterNames();
        String param = null;
        String value = null;
        int i = 0;
        HashMap qMap = new HashMap();
        while (params.hasMoreElements()) {
            param = (String) params.nextElement();
            value = request.getParameter(param);
            String mode = "";// 查询模式
            if (param.startsWith("_") || param.startsWith("$CODEVALUE_")) {
                continue;
            }
            if (value == null || value.equalsIgnoreCase("null")) {
                continue;
            }
            if (value.trim().length() == 0) {
                continue;
            }
            value = value.trim();
            if (param.indexOf("$Q_") != -1) {
                i++;
                if (i != 1) {
                    buf.append(" and ");
                }
                // 字段名
                buf.append(param.substring(param.indexOf("$Q_") + 3));
                // 保存查询条件
                qMap.put(param, value);
                // 防止'注入
                if (value.indexOf("'") != -1) {
                    value = value.replaceAll("'", "''");
                }
                String temp = param.substring(0, param.indexOf("$Q_"));
                if (!COMMON.isEmpty(temp)) {
                    mode = temp;
                }
                // 查询值
                // 是否是时间
                boolean timeFlag = false;
                if (value.indexOf("-") != -1) {
                    String[] valueDatas = value.split("-");
                    if (valueDatas != null && valueDatas.length == 2) {
                        if (COMMON.isDigitalString(valueDatas[0]) && COMMON.isDigitalString(valueDatas[1])) {
                            int yearTag = Integer.parseInt(valueDatas[0]);
                            int monthTag = Integer.parseInt(valueDatas[1]);
                            if (yearTag < 2100 && yearTag > 1900 && monthTag >= 1 && monthTag < 13) {
                                value = value + "-01";
                            }
                        }

                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
                    SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
                    try {
                        dateFormat.parse(value);
                        timeFlag = true;
                    } catch (ParseException e) {
                        try {
                            timeFormat.parse(value);
                            timeFlag = true;
                        } catch (ParseException e1) {
                            timeFlag = false;
                        }
                    }
                }

                // 查询方式
                if (isEmpty(mode)) {
                    // 默认查询模式
                    if (timeFlag) {
                        mode = "=";
                    } else {
                        mode = "like";
                    }
                }
                
                if(mode.length()!=0 && mode.equals("#=")){
                	buf.append(" = ");
                }else{
                	buf.append(" " + mode + " ");
                }

                if (!timeFlag && mode.length()!=0 && !mode.equals("#=")) {
                    buf.append(" '");
                }else{
                	 buf.append(" ");
                }
                if (mode.equals("like")) {
                    buf.append("%");
                }

                if (timeFlag) {
                    if (value.indexOf(":") == -1 && mode.indexOf("<") != -1 ) {
                        value += " 23:59:59";
                    }
                    if(value.indexOf(":") == -1 && mode.indexOf(">") != -1){
                        value += " 00:00:00";
                    }
                    if (DB_DIALECT.equals("ORACLE") || DB_DIALECT.equals("oracle")) {
                        buf.append("To_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
                    } else {
                        buf.append("'" + value + "'");
                    }
                } else {
                    buf.append(value);
                }
                if (mode.equals("like")) {
                    buf.append("%");
                }
                if (!timeFlag && mode.length()!=0 && !mode.equals("#=")) {
                    buf.append("'");
                }else{
                	 buf.append(" ");
                }
            }
        }

        request.setAttribute("qMap", qMap);
        return buf.toString();
    }
 
    /**
     * 字符串是否为数字
     * 
     * @param str
     * @return
     */
    public static boolean isDigitalString(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            try {
                Double.parseDouble(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
    /**
     * 得到参与者的连接串
     * @param joinners
     * @return
     */
	public static String getjoinner(String joinners){
		
		if(joinners!=null&&!joinners.equals(""))
		{
		String[] join=joinners.split(",");
		String[] newjoin=new String[join.length];
		for(int j=0;j<join.length;j++)
		{
			newjoin[join.length-1-j]=join[j];
		}
		
		
		
		String r="";
		for(int i=0;i<newjoin.length;i++){
			if(r.equals("")){
				r=newjoin[i].split("&")[0];
			}else{
				r=r+","+newjoin[i].split("&")[0];
			}
		}
		return r;
		}
		return "";
	}
   
    /**
     * 获取系统唯一ID
     * 
     * @return
     */
    public static String getUUID() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String tempId = sf.format(new Date());
        if (Long.parseLong(uid) >= Long.parseLong(tempId))
            uid = (Long.parseLong(uid) + 1) + "";
        else
            uid = tempId;
        return uid+getRandomString(5) + "i";
    }
    
    public static String getRandomString(int size) {
        char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; ++i)
            sb.append(c[(Math.abs(random.nextInt()) % c.length)]);

        return sb.toString();
    }
    
    public static String toHtmlStr(String src)
    {
      if ((src == null) || (src.length() == 0)) {
        return "";
      }
      StringBuffer aimStrBuf = new StringBuffer();
      StringBuffer resStrBuf = new StringBuffer(src);
      int strLength = resStrBuf.length();
      char tmpChar = '\0';
      for (int i = 0; i < strLength; ++i) {
        tmpChar = resStrBuf.charAt(i);
        if (tmpChar == '<')
          aimStrBuf.append(new char[] { '&', 'l', 't', ';' });
        else if (tmpChar == '<')
          aimStrBuf.append(new char[] { '&', 'g', 't', ';' });
        else if (tmpChar == '\n')
          aimStrBuf.append(new char[] { '<', 'b', 'r', '>' });
        else if (tmpChar != '\r') {
          if (tmpChar == '\t')
            aimStrBuf.append(new char[] { '&', 'n', 'b', 's', 'p', ';', 
              '&', 'n', 'b', 's', 'p', ';', '&', 'n', 'b', 's', 'p', 
              ';', '&', 'n', 'b', 's', 'p', ';' });
          else if (tmpChar == ' ')
            aimStrBuf.append(new char[] { '&', 'n', 'b', 's', 'p', ';' });
          else if (tmpChar == '"')
            aimStrBuf.append(new char[] { '\\', 8220 });
          else if (tmpChar == '\'')
            aimStrBuf.append(new char[] { '\\', '\'' });
          else if (tmpChar == '\\')
            aimStrBuf.append(new char[] { '\\', '\\' });
          else
            aimStrBuf.append(tmpChar);
        }
      }
      return aimStrBuf.toString();
    }

    public static float getProbability(float f){
    	String ff=f+"";
    	ff=ff.substring(0, 3);
    	float v=Float.valueOf(ff);
    	int vv=(int)(v/0.1);
    	if(vv>50){
    		return Float.valueOf("0.125");
    	}
    	v=vv*Float.valueOf("0.0008");
    	return Float.valueOf(v);
    }
    /**
     * duanzj 解码
     * @param nameString
     * @param decodeType
     * @return
     */
    public static String decode( String nameString ,String decodeType ){
    	try {
			return URLDecoder.decode ( nameString , decodeType ) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace() ;
		}
		return  null ;
    }
    
    /**
     *  转码
     * @param nameString
     * @param decodeType
     * @return String
     * @author chenhao
     */
    
    public static String encode( String nameString ,String decodeType ){
    	try {
			return URLEncoder.encode(nameString,decodeType);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace() ;
		}
		return  null ;
    }
    
    
    
    /** 
     * 获得当前时间 
     * @param parrten 输出的时间格式 
     * @return 返回时间 
     */ 
    public static String getTime(String parrten) 
    { 
      String timestr; 
      if(parrten==null||parrten.equals("")) 
      { 
        parrten="yyyyMMddHHmmss"; 
      } 
      java.text.SimpleDateFormat sdf=new SimpleDateFormat(parrten); 
      java.util.Date cday=new Date(); 
      timestr=sdf.format(cday); 
      return timestr; 
    } 

    /** 
     * 比较两个字符串时间的大小 
     * @param t1 时间1 
     * @param t2 时间2 
     * @param parrten 时间格式 :yyyy-MM-dd HH:mm:ss
     * @return 返回long =0相等，>0 t1>t2，<0 t1<t2 
     */ 
    public static long compareStringTime(String t1,String t2,String parrten) 
    { 
    	        java.text.SimpleDateFormat   formatter   =   new   java.text.SimpleDateFormat(parrten); 
    	        java.text.ParsePosition   pos   =   new   java.text.ParsePosition(0); 
    	        java.text.ParsePosition   pos1  =   new   java.text.ParsePosition(0); 
    	        java.util.Date   dt1   =  formatter.parse(t1,pos); 
    	        java.util.Date   dt2   =  formatter.parse(t2,pos1); 
    	        long l = dt1.getTime()-dt2.getTime(); 
    	        return   l; 
    } 

   
}
