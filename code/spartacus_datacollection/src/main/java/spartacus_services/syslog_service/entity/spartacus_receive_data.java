package spartacus_services.syslog_service.entity;

public class spartacus_receive_data 
{
	private String hostip;
	private long receivetime;
	private String receivesource;
	private String datacontent;
	private int idspartacus_assets;
	private String assetsname;
	private String securitydomain;
	private String businessdomain;
	private String physicalposition;
	private String subnodename;
	private String subnodeip;
	public String getHostip() {
		return hostip;
	}
	public void setHostip(String hostip) {
		this.hostip = hostip;
	}
	public long getReceivetime() {
		return receivetime;
	}
	public void setReceivetime(long receivetime) {
		this.receivetime = receivetime;
	}
	public String getReceivesource() {
		return receivesource;
	}
	public void setReceivesource(String receivesource) {
		this.receivesource = receivesource;
	}
	public String getDatacontent() {
		return datacontent;
	}
	public void setDatacontent(String datacontent) {
		this.datacontent = datacontent;
	}
	public int getIdspartacus_assets() {
		return idspartacus_assets;
	}
	public void setIdspartacus_assets(int idspartacus_assets) {
		this.idspartacus_assets = idspartacus_assets;
	}
	public String getAssetsname() {
		return assetsname;
	}
	public void setAssetsname(String assetsname) {
		this.assetsname = assetsname;
	}
	public String getSecuritydomain() {
		return securitydomain;
	}
	public void setSecuritydomain(String securitydomain) {
		this.securitydomain = securitydomain;
	}
	public String getBusinessdomain() {
		return businessdomain;
	}
	public void setBusinessdomain(String businessdomain) {
		this.businessdomain = businessdomain;
	}
	public String getPhysicalposition() {
		return physicalposition;
	}
	public void setPhysicalposition(String physicalposition) {
		this.physicalposition = physicalposition;
	}
	public String getSubnodename() {
		return subnodename;
	}
	public void setSubnodename(String subnodename) {
		this.subnodename = subnodename;
	}
	public String getSubnodeip() {
		return subnodeip;
	}
	public void setSubnodeip(String subnodeip) {
		this.subnodeip = subnodeip;
	}
}
