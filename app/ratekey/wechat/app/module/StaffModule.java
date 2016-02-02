package ratekey.wechat.app.module;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import ratekey.wechat.app.App;
import ratekey.wechat.app.domain.Staff;
import ratekey.wechat.app.service.DictService;
import ratekey.wechat.app.service.StaffService;
import ratekey.wechat.base.dto.DataGrid;
import ratekey.wechat.base.dto.Result;

@IocBean
@At("/staff")
public class StaffModule {
	
	private static final Log log = Logs.get();
	
	@Inject
	private StaffService staffService;
	@Inject
	private DictService dictService;
	
	@At("/")
	@Ok("json")
	public Staff get(Integer id) {
		return  staffService.fetch(id);
	}
	@At("/")
	@POST
	@Ok("json")
	@AdaptBy(type=JsonAdaptor.class)
	public Result insert(Staff staff) {
		if (Strings.isBlank(staff.getPassword())) {
			staff.setPassword(String.valueOf(App.getDefaultPassword().hashCode()));
		}
		return staffService.save(staff, null, null);
	}
	@At("/")
	@DELETE
	@Ok("json")
	public Result delete(Integer id) {
		return staffService.delete(id, null, null);
	}
	
	
	@At
	@Ok("json")
	public DataGrid all(String title) {
		DataGrid datagrid= staffService.datagrid();
		return datagrid;
	}
	
	@At
	@Ok("json")
	public Result login(String logName, String password, HttpSession session) {
		Staff usr = staffService.fetch(Cnd.where("logName", "=", logName));
		if (usr != null && String.valueOf(password.hashCode()).equals(usr.getPassword())) {
			session.setAttribute(Staff.SESSION_KEY, usr );
			return Result.ok("登录成功！", usr);
		}
		return  Result.err("用户名或密码错误！");
	}
	
	@At
	@Ok("json")
	public Result currentUsr(HttpSession session) {
		if (session.getAttribute(Staff.SESSION_KEY) != null) {
			Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
			return Result.ok("用户已登录！", usr);
		}
		return  Result.err("用户未登录！");
	}
	
	@At
	@Ok("json")
	public Result changePassword(String oldPassword, String newPassword, HttpSession session) {
		if (session.getAttribute(Staff.SESSION_KEY) != null) {
			Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
			if (String.valueOf(oldPassword.hashCode()).equals(usr.getPassword())) {
				usr.setPassword(String.valueOf(newPassword.hashCode()));
				session.setAttribute(Staff.SESSION_KEY, usr);
				staffService.update(usr);
				return Result.ok("修改成功！", usr);
			} else {
				return Result.err("密码输入错误！");
			}
		}
		return  Result.err("用户未登录！");
	}

	@At
	@Ok("json")
	public Result logout(HttpSession session) {
			session.setAttribute(Staff.SESSION_KEY, null );
			return Result.ok("登出成功！");
	}
}
