package muchon.wechat.app.domain.wechat;

/***
 * 模板消息嵌套数据
 * @author zhengcc
 *
 */
public class TemplateMsgDataProp {

	private String value;
	private String color;
	
	public TemplateMsgDataProp() {
		
	}
	
	public TemplateMsgDataProp(String value, String color) {
		this.value = value;
		this.color = color;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
