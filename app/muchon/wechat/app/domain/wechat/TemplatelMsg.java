package muchon.wechat.app.domain.wechat;

import java.util.HashMap;
import java.util.Map;

/***
 * 模板消息
 * @author zhengcc
 *
 */
public class TemplatelMsg {

	private String touser;
	private String template_id;
	private String url;
	private Map<String, TemplateMsgDataProp> data;
	
	public TemplatelMsg() {
		data = new HashMap<String, TemplateMsgDataProp>();
	}
	public TemplatelMsg(String openId, String templateId, String url) {
		this();
		this.touser = openId;
		this.template_id = templateId;
		this.url = url;
	}
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, TemplateMsgDataProp> getData() {
		return data;
	}
	public void setData(Map<String, TemplateMsgDataProp> data) {
		this.data = data;
	}
}
