package com.grm.logbacktest.controller;


import com.grm.logbacktest.logback.TopLogger;
import com.grm.logbacktest.model.User;
import com.grm.logbacktest.model.UserLog;
import com.grm.logbacktest.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guorumeng
 * @version 1.0.0
 * @Description controller层父类，完成日志记录到数据库
 * @date 2018/5/10
 */
public class WebBaseController {
    /** 文件日志记录器 */
    private static final Logger loggerRoot = LoggerFactory.getLogger("root");
    /** 数据库记录器 */
    private static final TopLogger loggerUser = new TopLogger("UserLogger");

    /**
     * 记录
     * @param info
     */
    protected void info(UserLog info){
        //填充日志信息
        info.setLevel(1);
        init(info);
        //存入数据库
        loggerUser.info(info);
        //标准日志
        loggerRoot.info(info.toString());
    }

    /**
     * 错误
     * @param error
     */
    protected void error(UserLog error){
        //填充日志信息
        error.setLevel(0);
        init(error);
        //存入数据库
        loggerUser.error(error);
        //标准日志
        loggerRoot.error(error.toString());
    }

    /**
     * 这里可以对userLog填充，在controller中使用时，只需要设置description
     * 因为用户存在session中，所以这里从session中取出用户信息，然后填充到userLog中
     * @param  - 用户操作日志
     */
    private void init(UserLog log){

        boolean validate = false;
        //获取当前会话客户端IP地址信息并填充到日志模型实例中
        String ip = HttpUtil.getIpAddr();
        log.setIp(ip);
        //获取当前会话中的用户和组织机构信息并填充到日志模型实例中
        User userInfo = HttpUtil.getSessionUser();
        UserLog userLog = (UserLog)log;
        userLog.setAccount(userInfo.getAccount());
        userLog.setUserId(userInfo.getId());
        userLog.setUserName(userInfo.getName());
        userLog.setOrgCode(userInfo.getOrgCode());
        userLog.setOrgName(userInfo.getOrgName());
    }
}
