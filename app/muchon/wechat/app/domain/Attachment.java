package muchon.wechat.app.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import muchon.wechat.base.domain.Constable;
import muchon.wechat.base.domain.IdEntity;

/***
 *  Value Object
 * 
 * @author ZCC
 * 
 */
@Table("Attachment")
public class Attachment  implements Serializable, IdEntity, Constable {

    private static final long serialVersionUID = -180208392254896918L;
    @Id
    private Integer id; // Id
    @Column
    private String localUrl; // 本地链接
    @Column
    private String wechatUrl; // 微信链接
    @Column
    private String type; // 类型
    @Column
    private String desc; // 描述
    @Column
    private String wechatMediaId; // 微信媒体ID

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLocalUrl() {
		return localUrl;
	}
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}
	public String getWechatUrl() {
		return wechatUrl;
	}
	public void setWechatUrl(String wechatUrl) {
		this.wechatUrl = wechatUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getWechatMediaId() {
		return wechatMediaId;
	}
	public void setWechatMediaId(String wechatMediaId) {
		this.wechatMediaId = wechatMediaId;
	}
	@Override
	public Map<String, String> getConstFieldMap() {
		Map<String, String> fm = new HashMap<String, String>();
		return fm;
	}
	@Override
	public void wrap() {
		// TODO Auto-generated method stub
		
	}
}