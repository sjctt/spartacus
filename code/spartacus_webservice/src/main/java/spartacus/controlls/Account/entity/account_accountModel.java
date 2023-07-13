package spartacus.controlls.Account.entity;
/**
 * @author Lix
 * @category 用户表的对象
 * @serial
 *【2019年7月29日】	建立对象
 */
public class account_accountModel 
{
	private int id;
	private String account;
	private String password;
	private String name;
	private String jobNumber;
	private int accountStatus;
	private String phoneNumber;
	private String email;
	private int pwdErrorTimes;
	private int lockTime;
	private String lastLoginTime;
	private String remark;
	private String createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public int getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPwdErrorTimes() {
		return pwdErrorTimes;
	}
	public void setPwdErrorTimes(int pwdErrorTimes) {
		this.pwdErrorTimes = pwdErrorTimes;
	}
	public int getLockTime() {
		return lockTime;
	}
	public void setLockTime(int lockTime) {
		this.lockTime = lockTime;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
