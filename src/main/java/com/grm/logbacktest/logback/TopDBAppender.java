package com.grm.logbacktest.logback;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.db.ConnectionSource;
import ch.qos.logback.core.db.DBHelper;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import com.mysql.jdbc.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义logback日志存入数据库的dbappender
 * logback.xml中引用
 */
public class TopDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	static final StackTraceElement EMPTY_CALLER_DATA = CallerData.naInstance();
	private ConnectionSource connectionSource;
	private String insertsql;
	private String params;
	private boolean iscolseConnection;
	private String dateformat;

	public TopDBAppender() {
		this.iscolseConnection = true;
	}

	/**
	 * 添加事件信息
	 */
	protected void append(ILoggingEvent eventObject) {
		if (this.getConnectionSource() == null) {
			addError("未配置数据源，同时也未指定全局数据源。");
			return;
		}
		Connection connection = null;
		try {
            connection = this.connectionSource.getConnection();
			connection.setAutoCommit(false);

			PreparedStatement insertStatement = connection.prepareStatement(getInsertSQL());
			synchronized (this) {
				subAppend(eventObject, connection, insertStatement);
			}
			insertStatement.close();
			connection.commit();
		} catch (Throwable e) {
			addError("problem appending event", e);
		} finally {
			if (this.iscolseConnection){
				DBHelper.closeConnection(connection);
			}
		}
	}

	/**
	 * 读取写入的SQL
	 * @return
	 */
	protected String getInsertSQL() {
		if (StringUtils.isEmptyOrWhitespaceOnly(this.insertsql)) {
			throw new RuntimeException("TopDBAppender未配置<insertsql></insertsql>属性");
		}
        return this.insertsql;
	}

	/**
	 * 处理SQL参数值
	 * @param eventObject
	 * @param connection
	 * @param statement
	 * @throws Throwable
	 */
	private void subAppend(ILoggingEvent eventObject, Connection connection,
                           PreparedStatement statement) throws Throwable {
		if (StringUtils.isEmptyOrWhitespaceOnly(this.params)) {
			throw new RuntimeException("TopDBAppender未配置<params></params>属性");
		}
		String[] split = this.params.split(",");
		putparmas(split, eventObject, statement);
		int updateCount = statement.executeUpdate();
		if (updateCount != 1){
			addWarn("Failed to insert loggingEvent");
		}
	}

	/**
	 * 填充SQL参数值
	 * @param split
	 * @param eventObject
	 * @param statement
	 * @throws SQLException
	 */
	private void putparmas(String[] split, ILoggingEvent eventObject,
                           PreparedStatement statement) throws SQLException {
		StackTraceElement[] callerData = eventObject.getCallerData();
		for (int i = 0; i < split.length; ++i) {
			String temp = changePattern(eventObject, callerData, split[i]);
			temp = StringUtils.isEmptyOrWhitespaceOnly(temp) ?null :temp;
			statement.setObject(i + 1, temp);
		}
	}

	/**
	 * 解析自定义参数装载
	 * @param eventObject
	 * @param callerData
	 * @param patternkey
	 * @return
	 */
	private String changePattern(ILoggingEvent eventObject,
                                 StackTraceElement[] callerData, String patternkey) {
		StackTraceElement caller = extractFirstCaller(callerData);
		if (patternkey.contains("%id")) {
			patternkey = patternkey.replaceAll("%id", eventObject.getMDCPropertyMap().get("id"));
		}
		if (patternkey.contains("%caller")) {
			patternkey = patternkey.replaceAll("%caller",
					caller.getFileName() + "#" + caller.getClassName() + "#"
							+ caller.getMethodName() + "#"
							+ Integer.toString(caller.getLineNumber()));
		}
		if (patternkey.contains("%date")) {
			patternkey = patternkey.replaceAll("%date",
					new SimpleDateFormat(this.dateformat).format(new Date(
							eventObject.getTimeStamp())));
		}
		if (patternkey.contains("%d")) {
			patternkey = patternkey.replaceAll("%d",
					new SimpleDateFormat(this.dateformat).format(new Date(
							eventObject.getTimeStamp())));
		}
		if (patternkey.contains("%logger")) {
			patternkey = patternkey.replaceAll("%logger", eventObject.getLoggerName());
		}
		if (patternkey.contains("%lo")) {
			patternkey = patternkey.replaceAll("%lo", eventObject.getLoggerName());
		}
		if (patternkey.contains("%c")) {
			patternkey = patternkey.replaceAll("%c", eventObject.getLoggerName());
		}
		if (patternkey.contains("%L")) {
			patternkey = patternkey.replaceAll("%L", String.valueOf(caller.getLineNumber()));
		}
		if (patternkey.contains("%line")) {
			patternkey = patternkey.replaceAll("%line", String.valueOf(caller.getLineNumber()));
		}
		if (patternkey.contains("%message")) {
			patternkey = patternkey.replaceAll("%message", eventObject.getMessage());
		}
		if (patternkey.contains("%msg")) {
			patternkey = patternkey.replaceAll("%msg", eventObject.getMessage());
		}
		if (patternkey.contains("%m")) {
			patternkey = patternkey.replaceAll("%m", eventObject.getMessage());
		}
		if (patternkey.contains("%n")) {
			patternkey = patternkey.replaceAll("%n", "\r\n");
		}
		if (patternkey.contains("%p")) {
			patternkey = patternkey.replaceAll("%p", eventObject.getLevel().levelStr);
		}
		if (patternkey.contains("%le")) {
			patternkey = patternkey.replaceAll("%le", eventObject.getLevel().levelStr);
		}
		if (patternkey.contains("%level")) {
			patternkey = patternkey.replaceAll("%level", eventObject.getLevel().levelStr);
		}
		if (patternkey.contains("%t")) {
			patternkey = patternkey.replaceAll("%t", eventObject.getThreadName());
		}
		if (patternkey.contains("%thread")) {
			patternkey = patternkey.replaceAll("%thread", eventObject.getThreadName());
		}
		if (patternkey.contains("%X")) {
			Map<?, ?> mdcPropertyMap = eventObject.getMDCPropertyMap();
			Pattern pattern = Pattern.compile("\\%X\\{\\w+\\}");
			Matcher matcher = pattern.matcher(patternkey);
			String newstr = new String(patternkey);
			while (matcher.find()) {
				String temp = matcher.group();
				String mdctemp = (String) mdcPropertyMap.get(temp.substring(3, temp.length() - 1));
				if (mdctemp != null) {
					newstr = newstr.replace(temp, mdctemp);
				}else{
					addWarn(temp + "在MDC中未put值");
					newstr = newstr.replace(temp, "");
				}
			}
			patternkey = newstr;
		}
		return patternkey;
	}

	/**
	 * 参数值元素弹栈转换
	 * @param callerDataArray
	 * @return
	 */
	private StackTraceElement extractFirstCaller(
			StackTraceElement[] callerDataArray) {
		StackTraceElement caller = EMPTY_CALLER_DATA;
		if (hasAtLeastOneNonNullElement(callerDataArray))
			caller = callerDataArray[0];
		return caller;
	}

	/**
	 * 至少有一个非空元素
	 * @param callerDataArray
	 * @return
	 */
	private boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
		return (callerDataArray != null) && (callerDataArray.length > 0)
				&& (callerDataArray[0] != null);
	}

	public boolean isIscolseConnection() {
		return this.iscolseConnection;
	}

	public void setIscolseConnection(boolean iscolseConnection) {
		this.iscolseConnection = iscolseConnection;
	}

	public String getInsertsql() {
		return this.insertsql;
	}

	public void setInsertsql(String insertsql) {
		this.insertsql = insertsql;
	}

	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getDateformat() {
		return this.dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	public void setConnectionSource(ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

	public ConnectionSource getConnectionSource() {
		/*
		 * 通过加载全局数据源，来初始化日志装载器
		 * 若连续三次失败，则停止本次初始化
		 */
		while(null==this.connectionSource){
			if(ApplicationContextHolder.hasApplicationContext()){
				ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
				DataSourceConnectionSource dataSourceConnectionSource = new DataSourceConnectionSource();
				dataSourceConnectionSource.setDataSource(applicationContext.getBean(DataSource.class));
				this.connectionSource = dataSourceConnectionSource;
			}else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					addError("初始化日志装载器失败：加载全局数据源时进程异常", e);
				}
			}
		}
		return this.connectionSource;
	}
}