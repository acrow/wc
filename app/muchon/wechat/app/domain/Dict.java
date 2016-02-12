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
@Table("Dict")
public class Dict  implements Serializable, IdEntity, Constable {

    private static final long serialVersionUID = -180208392254896918L;
    @Id
    private Integer id; // Id
    @Column
    private String type; // 字典类型
    @Column
    private String name; // 字典文字
    @Column
    private String value; // 字典值
    @Column
    private Integer sortIndex; // 排序
    @Column
    private String description; // 说明

    /***
     * 取得Id
     * @return
     */
    public Integer getId() {
        return id;
    }
    /***
     * 设置Id
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /***
     * 取得字典类型
     * @return
     */
    public String getType() {
        return type;
    }
    /***
     * 设置字典类型
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /***
     * 取得字典文字
     * @return
     */
    public String getName() {
        return name;
    }
    /***
     * 设置字典文字
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * 取得字典值
     * @return
     */
    public String getValue() {
        return value;
    }
    /***
     * 设置字典值
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /***
     * 取得排序
     * @return
     */
    public Integer getSortIndex() {
        return sortIndex;
    }
    /***
     * 设置排序
     * @param sortIndex
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    /***
     * 取得说明
     * @return
     */
    public String getDescription() {
        return description;
    }
    /***
     * 设置说明
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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