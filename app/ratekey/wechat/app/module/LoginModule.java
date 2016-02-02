package ratekey.wechat.app.module;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import ratekey.wechat.app.domain.Staff;
import ratekey.wechat.app.service.StaffService;

@IocBean
@At("/")
public class LoginModule {
	
	private static final Log log = Logs.get();
	
	@Inject
	private StaffService staffService;
	
	@At
	@Ok("jsp:/login")
	@GET
	public void login() {
		
	}
	@At
	@Ok(">>:/main")
	@Fail("jsp:/login")
	@POST
	public Object login(String logName, String password, HttpSession session) throws Exception {
		log.debug("login (" + logName + ", " + password + ") " + password.hashCode());
		Staff usr = staffService.fetch(Cnd.where("logName", "=", logName));
		if (usr != null && String.valueOf(password.hashCode()).equals(usr.getPassword())) {
			session.setAttribute(Staff.SESSION_KEY, usr );
			return null;
		}
		throw new Exception("用户名或密码错误！");
	}
	@At
	@Ok("jsp:/main")
	public void main() {
		
	}
}
