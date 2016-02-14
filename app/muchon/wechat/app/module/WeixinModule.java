package muchon.wechat.app.module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import muchon.wechat.base.util.WxHandler;

@IocBean
@At("/")
public class WeixinModule {
	
	private static final Log log = Logs.get();
	
	
	@At
	@Ok("raw")
	public String ping() {
		return WxHandler.accessToken();
	}
	
	@At
	@Ok("raw")
	public String menu() {
		return WxHandler.menu();
	}
	
	@At
	@Ok("raw")
	public String userOpenIds() {
		return WxHandler.userOpenIds();
	}
	
	@At
	@Ok("raw")
	public String user(String openId) {
		return WxHandler.user(openId);
	}
	
	@At
	@Ok("raw")
	public String users(String openIds) {
		if (!Strings.isBlank(openIds)) {
			return WxHandler.users(openIds.split(","));
		} else {
			String ids = userOpenIds();
			Pattern pattern = Pattern.compile("\\[.*\\]");
			Matcher matcher = pattern.matcher(ids);
			if(matcher.find()) {
				ids = matcher.group(0);
				ids = ids.replace("\"", "").replace("[", "").replace("]", "");
				return WxHandler.users(ids.split(","));
			}
			return "";
		}
	}
	
	@At
	@Ok("raw")
	public String groups() {
		return WxHandler.groups();
	}
	
	@At
	@Ok("raw")
	public String groupCreate(String name) {
		return WxHandler.groupCreate(name);
	}
	
	@At
	@Ok("raw")
	public String groupDelete(String id) {
		return WxHandler.groupDelete(id);
	}
}
