package com.grm.logbacktest.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.StatusManager;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TopLogger implements Logger {
	/**
	 * Logback上下文实例
	 */
	private static final LoggerContext context;
	static {
		context = (LoggerContext) LoggerFactory.getILoggerFactory();
	}
	/**
	 * 组合属性
	 * 代理基本日志记录器行为
	 */
	private Logger loger;

	public TopLogger(Class<?> clazz) {
		loger = LoggerFactory.getLogger(clazz);
	}

	public TopLogger(String classname) {
		loger = LoggerFactory.getLogger(classname);
	}

	public String getName() {
		return loger.getName();
	}

	public boolean isTraceEnabled() {
		return loger.isTraceEnabled();
	}

	public void trace(String msg) {
		loger.trace(msg);
	}

	public void trace(String format, Object arg) {
		loger.trace(format, arg);
	}

	public void trace(String format, Object arg1, Object arg2) {
		loger.trace(format, arg1, arg2);
	}

	public void trace(String format, Object... arguments) {
		loger.trace(format, arguments);
	}

	public void trace(String msg, Throwable t) {
		loger.trace(msg, t);
	}

	public boolean isTraceEnabled(Marker marker) {
		return loger.isTraceEnabled(marker);
	}

	public void trace(Marker marker, String msg) {
		loger.trace(marker, msg);
	}

	public void trace(Marker marker, String format, Object arg) {
		loger.trace(marker, format, arg);
	}

	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		loger.trace(marker, format, arg1, arg2);
	}

	public void trace(Marker marker, String format, Object... argArray) {
		loger.trace(marker, format, argArray);
	}

	public void trace(Marker marker, String msg, Throwable t) {
		loger.trace(marker, msg, t);
	}

	public boolean isDebugEnabled() {
		return loger.isDebugEnabled();
	}

	public void debug(String msg) {
		loger.debug(msg);
	}

	public void debug(String format, Object arg) {
		loger.debug(format, arg);
	}

	public void debug(String format, Object arg1, Object arg2) {
		loger.debug(format, arg1, arg2);
	}

	public void debug(String format, Object... arguments) {
		loger.debug(format, arguments);
	}

	public void debug(String msg, Throwable t) {
		loger.debug(msg, t);
	}

	public boolean isDebugEnabled(Marker marker) {
		return loger.isDebugEnabled(marker);
	}

	public void debug(Marker marker, String msg) {
		loger.debug(marker, msg);
	}

	public void debug(Marker marker, String format, Object arg) {
		loger.debug(marker, format, arg);
	}

	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		loger.debug(marker, format, arg1, arg2);
	}

	public void debug(Marker marker, String format, Object... arguments) {
		loger.debug(marker, format, arguments);
	}

	public void debug(Marker marker, String msg, Throwable t) {
		loger.debug(marker, msg, t);
	}

	public boolean isInfoEnabled() {
		return loger.isInfoEnabled();
	}

	public void info(String msg) {
		loger.info(msg);
	}

	public void info(String format, Object arg) {
		loger.info(format, arg);
	}

	public void info(String format, Object arg1, Object arg2) {
		loger.info(format, arg1, arg2);
	}

	public void info(String format, Object... arguments) {
		loger.info(format, arguments);
	}

	public void info(String msg, Throwable t) {
		loger.info(msg, t);
	}

	public boolean isInfoEnabled(Marker marker) {
		return loger.isInfoEnabled(marker);
	}

	public void info(Marker marker, String msg) {
		loger.info(marker, msg);
	}

	public void info(Marker marker, String format, Object arg) {
		loger.info(marker, format, arg);
	}

	public void info(Marker marker, String format, Object arg1, Object arg2) {
		loger.info(marker, format, arg1, arg2);
	}

	public void info(Marker marker, String format, Object... arguments) {
		loger.info(marker, format, arguments);
	}

	public void info(Marker marker, String msg, Throwable t) {
		loger.info(marker, msg, t);
	}

	public boolean isWarnEnabled() {
		return loger.isWarnEnabled();
	}

	public void warn(String msg) {
		loger.warn(msg);
	}

	public void warn(String format, Object arg) {
		loger.warn(format, arg);
	}

	public void warn(String format, Object... arguments) {
		loger.warn(format, arguments);
	}

	public void warn(String format, Object arg1, Object arg2) {
		loger.warn(format, arg1, arg2);
	}

	public void warn(String msg, Throwable t) {
		loger.warn(msg, t);
	}

	public boolean isWarnEnabled(Marker marker) {
		return loger.isWarnEnabled(marker);
	}

	public void warn(Marker marker, String msg) {
		loger.warn(marker, msg);
	}

	public void warn(Marker marker, String format, Object arg) {
		loger.warn(marker, format, arg);
	}

	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		loger.warn(marker, format, arg1, arg2);
	}

	public void warn(Marker marker, String format, Object... arguments) {
		loger.warn(marker, format, arguments);
	}

	public void warn(Marker marker, String msg, Throwable t) {
		loger.warn(marker, msg, t);
	}

	public boolean isErrorEnabled() {
		return loger.isErrorEnabled();
	}

	public void error(String msg) {
		loger.error(msg);
	}

	public void error(String format, Object arg) {
		loger.error(format, arg);
	}

	public void error(String format, Object arg1, Object arg2) {
		loger.error(format, arg1, arg2);
	}

	public void error(String format, Object... arguments) {
		loger.error(format, arguments);
	}

	public void error(String msg, Throwable t) {
		loger.error(msg, t);
	}

	public boolean isErrorEnabled(Marker marker) {
		return loger.isErrorEnabled(marker);
	}

	public void error(Marker marker, String msg) {
		loger.error(marker, msg);
	}

	public void error(Marker marker, String format, Object arg) {
		loger.error(marker, format, arg);
	}

	public void error(Marker marker, String format, Object arg1, Object arg2) {
		loger.error(marker, format, arg1, arg2);
	}

	public void error(Marker marker, String format, Object... arguments) {
		loger.error(marker, format, arguments);
	}

	public void error(Marker marker, String msg, Throwable t) {
		loger.error(marker, msg, t);
	}

	public void info(Log log) {
		putMDCproperties(log);
		info("");
	}

	public void error(Log log) {
		putMDCproperties(log);
		error("");
	}

	/**
	 * 个性化的，键值对动态组装车间
	 * 反射机制
     * 这个方法主要实现userLog对象中信息解析到logback-spring.xml文件中
	 * @param log
	 */
	private void putMDCproperties(Log log) {
		try {
			Method[] methods = log.getClass().getMethods();
			MDC.clear();
			for (int i = 0; i < methods.length; ++i) {
				Method method = methods[i];
				if ((!method.getName().startsWith("get"))
						|| (method.getName().equals("getClass"))) {
					continue;
				}
				String pro = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                Object invoke = method.invoke(log, (Object[]) null);
				String value = null;
				if (invoke instanceof Date) {
					Date date = (Date) invoke;
					value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				} else if(invoke instanceof Object) {
					value = invoke.toString();
				}
				if (!StringUtils.isEmptyOrWhitespaceOnly(pro)){
				    //这个话实现键值对解析到logback-spring.xml文件中
					MDC.put(pro, value);
				}
			}
		} catch (Exception e) {
			StatusManager sm = context.getStatusManager();
			ErrorStatus es = new ErrorStatus("Log属性解析失败！", null, e);
			sm.add(es);
		}
	}

}