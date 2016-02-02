package ratekey.wechat.app.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import ratekey.wechat.base.domain.Constable;
import ratekey.wechat.base.domain.IdEntity;

/***
 *  Value Object
 * 
 * @author ZCC
 * 
 */
@Table("Staff")
public class Staff  implements Serializable, IdEntity ,Constable{

    private static final long serialVersionUID = -181173048553557442L;
    
	public static final String SESSION_KEY = "usr";
	
    @Id
    private Integer id; // Id
    @Column
    private String name; // 姓名
    @Column
    private String logName; // 登录名
    @Column
    private String password; // 密码
    @Column
    private String role; // 角色

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
     * 取得姓名
     * @return
     */
    public String getName() {
        return name;
    }
    /***
     * 设置姓名
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * 取得登录名
     * @return
     */
    public String getLogName() {
        return logName;
    }
    /***
     * 设置登录名
     * @param logName
     */
    public void setLogName(String logName) {
        this.logName = logName;
    }

    /***
     * 取得密码
     * @return
     */
    public String getPassword() {
        return password;
    }
    /***
     * 设置密码
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /***
     * 取得角色
     * @return
     */
    public String getRole() {
        return role;
    }
    /***
     * 设置角色
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
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