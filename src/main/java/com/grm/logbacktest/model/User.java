package com.grm.logbacktest.model;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 暂时记录日志时使用，需要时可修改
 */
public class User implements Cloneable {

	private String id;//主键

	private String account;//账号

	private String password;//密码

	private String name;//姓名

	private String orgCode;//组织机构编码

	private String orgName;//组织机构名称

	/**
	 * 
	 */
	public User() {
		super();
	}

	/**
	 * @param account
	 * @param password
	 */
	public User(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	/**
	 * 判定信息完整性
	 * @return true 或 false
	 */
	public boolean validate() {
		boolean idOk =  Pattern.compile("\\w{15}").matcher(id).matches();
		boolean accountOk = null!=account && Pattern.compile("\\w{5,20}").matcher(account).matches();
		boolean nameOk = null!=name && Pattern.compile(".{2,20}").matcher(name).matches();
		boolean passwordOk =  (null!=password && Pattern.compile("[\\x00-\\x7f]{10,32}").matcher(password).matches());
		boolean state = idOk&&accountOk&&nameOk&&passwordOk;
		return state;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                '}';
    }
}
