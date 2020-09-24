package spartacus_controlls.spartacus_nodeinteractive.entity;

/**
* @author Song
* @category get_sysinfo 命令返回对象
*/
public class sysinfo 
{
	private int runstatus;
	private String nodename;
	public int getRunstatus() {
		return runstatus;
	}
	public void setRunstatus(int runstatus) {
		this.runstatus = runstatus;
	}
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	
}
