package spartacus_services.datacache_service.entity;
/**
 * @author Song
 * @category
 * @serial
 *【2019年9月27日】	建立对象
 */
public class spartacus_analyserule 
{
	private int idspartacus_analyserule;
	private String rulename;
	private int ruletype;
	private int analyzers;
	private int rulestatus;
	private String rulecontent;
	private String createtime;
	
	public int getIdspartacus_analyserule() {
		return idspartacus_analyserule;
	}
	public void setIdspartacus_analyserule(int idspartacus_analyserule) {
		this.idspartacus_analyserule = idspartacus_analyserule;
	}
	public String getRulename() {
		return rulename;
	}
	public void setRulename(String rulename) {
		this.rulename = rulename;
	}
	public int getRuletype() {
		return ruletype;
	}
	public void setRuletype(int ruletype) {
		this.ruletype = ruletype;
	}
	public int getAnalyzers() {
		return analyzers;
	}
	public void setAnalyzers(int analyzers) {
		this.analyzers = analyzers;
	}
	public int getRulestatus() {
		return rulestatus;
	}
	public void setRulestatus(int rulestatus) {
		this.rulestatus = rulestatus;
	}
	public String getRulecontent() {
		return rulecontent;
	}
	public void setRulecontent(String rulecontent) {
		this.rulecontent = rulecontent;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
