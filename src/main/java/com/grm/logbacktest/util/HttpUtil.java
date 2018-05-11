package com.grm.logbacktest.util;

import com.grm.logbacktest.model.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

public class HttpUtil {

	/**
	 * 端口的格式定义
	 */
	public final static Pattern PORT_PATTERN = Pattern.compile("[01]?[0-9]{1,4}|6[0-4][0-9]{1,3}|65[0-4][0-9]{1,2}|655[0-2][0-9]|6553[0-5]");

	/**
	 * IPv4与IPv6的格式定义
	 */
	public final static Pattern IP_PATTERN = Pattern.compile("([01]?[0-9]{0,2}|2[0-4][0-9]|25[0-5])(\\.([01]?[0-9]{0,2}|2[0-4][0-9]|25[0-5])){3}|([0-9a-fA-F]{4}(:[0-9a-fA-F]{4}){7})");

	/**
	 * 域名的格式定义
	 * 自定义约束：单词部分只允许单词字符的子集（不含_）
	 */
	public final static Pattern DOMAIN_PATTERN = Pattern.compile("[0-9a-zA-Z]+(\\.[0-9a-zA-Z]+)*");

	/**
	 * URL的格式定义
	 * 自定义约束：域名的单词部分只允许单词字符的子集（不含_）
	 */
	public final static Pattern URL_PATTERN = Pattern.compile("((HTTPS|HTTP|https|http)://)?([0-9a-zA-Z]+(\\.[0-9a-zA-Z]+)*|([01]?[0-9]{0,2}|2[0-4][0-9]|25[0-5])(\\.([01]?[0-9]{0,2}|2[0-4][0-9]|25[0-5])){3}|([0-9a-fA-F]{4}(:[0-9a-fA-F]{4}){7}))(:[01]?[0-9]{1,4}|:6[0-4][0-9]{1,3}|:65[0-4][0-9]{1,2}|:655[0-2][0-9]|:6553[0-5])?/?");

	/**
	 * 判断是否符合IP地址模式
	 * @param ips - 待检验IP地址串
	 * @param regex - 多个IP地址间隔符
	 * @return 格式检验结果
	 */
	public final static boolean isIps(String ips, String regex){
		boolean matches = null != ips && null != regex;
		if(matches) {
			for(String ip : ips.split(regex)){
				matches = matches && IP_PATTERN.matcher(ip).matches();
			}
		}
		return matches;
	}

	/**
	 * 获取当前http请求
	 * @return 当前http请求
	 */
	public final static HttpServletRequest getCurrentRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 获取当前会话中存储的参数
	 * @param key - 映射参数的键
	 * @return 参数
	 */
	public final static Object getSessionAttribute(final String key){
		HttpServletRequest request = getCurrentRequest();
		HttpSession session = null==request?null:request.getSession();
		return null==session?null:session.getAttribute(key);
	}
	
	/**
	 * 获取当前会话中存储的登录用户
	 * @return User实例
	 */
	public final static User getSessionUser(){
		Object obj = getSessionAttribute("sysuser");
		User user;
		if(null!=obj && obj instanceof User){
			user = (User)obj;
		}else{
			user = new User();
		}
		return user;
	}
	
	/**
	 * 获取客户端真实IP地址
	 * @return
	 */
	public final static String getIpAddr() {
		HttpServletRequest request = getCurrentRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		//如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}
	
	/**
	 * 获取客户端浏览器的用户代理头
	 * Navigator.userAgent	声明了浏览器用于 HTTP请求的用户代理头的值
	 * window.navigator.userAgent	属性是一个只读的字符串
	 * @return
	 */
	public final static String getUserAgent() {
		HttpServletRequest request = getCurrentRequest();
		String Agent = request.getHeader("User-Agent");
		return Agent;
	}

} 