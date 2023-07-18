package spartacus_servicecenter.test_spartacus_analyserule.entity;

import java.util.ArrayList;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月25日】	建立对象
 */
public class rulecontent 
{
	private String regular;
	private String splitchar;
	private String eventtype;
	private ArrayList<key_value> mapping;
	
	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}
	public String getSplitchar() {
		return splitchar;
	}
	public void setSplitchar(String splitchar) {
		this.splitchar = splitchar;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public ArrayList<key_value> getMapping() {
		return mapping;
	}
	public void setMapping(ArrayList<key_value> mapping) {
		this.mapping = mapping;
	}
}
