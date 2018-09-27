package com.stylefeng.guns.modular.lijun.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tool {
	public static String ISO_8859_1toUTF_8(String s) throws UnsupportedEncodingException{
		return new String(s.getBytes("ISO-8859-1"), "UTF-8");
	}
	/**
	 * 获取访问的域名
	 * @return
	 */
	public static String getDomain(){
		return ((HttpServletRequest)getRequest_Response_Session()[0]).getServerName();
	}
	/**
	 * 获取带http或https等开头的完整访问的域名
	 * @return
	 */
	public static String getHttpDomain(){
		StringBuffer url=((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURL();
		return url.delete(url.length() - ((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURI().length(), url.length()).append("/").toString();
	}
	public static String getImgUrlPrefix(){
		return (Tool.getHttpDomain()+FinalStaticString.FILEPATHIMG.split("/")[FinalStaticString.FILEPATHIMG.split("/").length-1]+"/");
	}
	/**
	 * 获取Ip地址
	 * @return
	 */
	public static String getIpAdrress() {
		HttpServletRequest request= (HttpServletRequest) getRequest_Response_Session()[0];
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	/**
	 * 辅助方法:判断字符串是否为空字符串或空
	 * @param string
	 * @return
	 */
	public static boolean isNull(Object string){
		if(string==null||"".equals(string.toString().trim())||"null".equals(string.toString().trim())){
			return true;
		}else{return false;}
	}
	/**
	 * 判断map里面是否有这个key,并且这key是否不为null,两者有一个否,就返回false
	 * @param m
	 * @param k
	 * @return
	 */
	public static boolean mapGetKeyNotEmpty(Map<String, Object> m,String k){
		return m.containsKey(k)&&m.get(k)!=null&&(m.get(k) instanceof String?m.get(k).toString().trim()!=""||m.get(k).toString().trim()!="null":true);
	}
	/**
	 * 辅助方法:判断集合是否为空
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean listIsNull(List<T>list){if(list==null||list.isEmpty()||list.size()==0||(list.size()==1&&list.get(0)==null))return true;return false;}
	/**
	 * HttpServletRequest从上下文中获取,HttpServletResponse和HttpSession从HttpServletRequest获取
	 * @return Object{HttpServletRequest,HttpServletResponse,HttpSession}
	 */
	public static final Object[] getRequest_Response_Session(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		List<Object>req_res_session=new ArrayList<>();
		if(sra!=null){
			req_res_session.add(sra.getRequest());
			ServletWebRequest servletWebRequest = new ServletWebRequest((HttpServletRequest)req_res_session.get(0));
			req_res_session.add(servletWebRequest.getResponse());
			req_res_session.add(((HttpServletRequest)req_res_session.get(0)).getSession());
			return req_res_session.toArray();
		}else{
			return null;
		}
	}

	/**
	 * 根据需要排序的字段数字,按顺序降序排列
	 * @param list
	 * @param keys
	 * @return
	 */
	public static final List<Map<String, Object>> ListMapOrderByMapKeyDesc(List<Map<String, Object>> list,final String [] keys){
	    Collections.sort(list,new Comparator<Map>() {
	          public int compare(Map o1, Map o2) {
	               return recursion(o1, o2, 0);
	          }
	          private int recursion(Map o1, Map o2, int i) {
	               if (o1.containsKey(keys[i]) && o2.containsKey(keys[i])) {
	                     Object value1 = o1.get(keys[i]);
	                     Object value2 = o2.get(keys[i]);
	                     if (value1 == null && value2 == null) {
	                          if ((i+1) < keys.length) {
	                                int recursion = recursion(o1, o2, i+1);
	                                return recursion;
	                          }else{
	                                return 0;
	                          }
	                     }else if(value1 == null && value2 != null){
	                          return 1;
	                     }else if(value1 != null && value2 == null){
	                          return -1;
	                     }else{
	                          if (value1.equals(value2)) {
	                                if ((i+1) < keys.length) {
	                                     return recursion(o1, o2, i+1);
	                                }else{
	                                     return 0;
	                                }
	                          }else{
	                                if (value1 instanceof String && value2 instanceof String) {
	                                     return value2.toString().compareTo(value1.toString());
	                                }else if(value1 instanceof Timestamp && value2 instanceof Timestamp){
	                                	return ((Timestamp)(value2)).compareTo(new Date(((Timestamp)(value1)).getTime()));
	                                }else{
	                                     return new BigDecimal(value2.toString()).compareTo(new BigDecimal(value1.toString()));
	                                }
	                          }
	                     }
	               }else{
	                     System.out.println(" ** The current map do not containskey : " + keys[i] + ",or The value of key is null **");
	                     return 0;
	               }
	          }
	    });
	    return list;
	}
	/**
	 * 随机生成六位数验证码
	 * @return
	 */
	public static int getRandomNum(){
		Random r = new Random();
		return r.nextInt(900000)+100000;//(Math.random()*(999999-100000)+100000)
	}
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate) throws Exception{
		String resultTimes = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now;
		now = new Date();
		Date date=df.parse(StrDate);
		long times = now.getTime()-date.getTime();
		long day  =  times/(24*60*60*1000);
		long hour = (times/(60*60*1000)-day*24);
		long min  = ((times/(60*1000))-day*24*60-hour*60);
		long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

		StringBuffer sb = new StringBuffer();
		//sb.append("发表于：");
		if(day>0 ){
			sb.append(day+"天前");
		}else if(hour>0 ){
			sb.append(hour+"小时前");
		} else if(min>0){
			sb.append(min+"分钟前");
		} else{
			sb.append(sec+"秒前");
		}
		resultTimes = sb.toString();
		return resultTimes;
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param strXML XML字符串
	 * @return XML数据转换后的Map
	 * @throws Exception
	 */
	public static Map<String, Object> xmlToMap(String strXML) throws Exception {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(),  element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception ex) {
			getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
			throw ex;
		}

	}
	/**
	 * 日志
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}
}
