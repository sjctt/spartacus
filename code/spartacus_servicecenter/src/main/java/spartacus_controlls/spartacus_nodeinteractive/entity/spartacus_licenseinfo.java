package spartacus_controlls.spartacus_nodeinteractive.entity;
/**
 * @author Song
 * @category
 * @serial
 *【2019年9月17日】	建立对象
 */
public class spartacus_licenseinfo 
{
	private String licensestatus;
	private String unitname;
	private String licensetype;
	private String licenseclass;
	private String expirytime;
	private long flow;
	private String errormessage;
	public String getLicensestatus() {
		return licensestatus;
	}
	public void setLicensestatus(String licensestatus) {
		this.licensestatus = licensestatus;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getLicensetype() {
		return licensetype;
	}
	public void setLicensetype(String licensetype) {
		this.licensetype = licensetype;
	}
	public String getLicenseclass() {
		return licenseclass;
	}
	public void setLicenseclass(String licenseclass) {
		this.licenseclass = licenseclass;
	}
	public String getExpirytime() {
		return expirytime;
	}
	public void setExpirytime(String expirytime) {
		this.expirytime = expirytime;
	}
	public long getFlow() {
		return flow;
	}
	public void setFlow(long flow) {
		this.flow = flow;
	}
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	
}
