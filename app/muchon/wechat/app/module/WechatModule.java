package muchon.wechat.app.module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import muchon.wechat.app.App;
import muchon.wechat.app.domain.Attachment;
import muchon.wechat.app.domain.wechat.TemplateMsgDataProp;
import muchon.wechat.app.domain.wechat.TemplatelMsg;
import muchon.wechat.app.service.AttachmentService;
import muchon.wechat.app.util.WechatHandler;

@IocBean
@At("/")
public class WechatModule {
	
	private static final Log log = Logs.get();
	@Inject
	private AttachmentService attachmentService;
	
	@At
	@Ok("raw")
	public String ping() {
		return WechatHandler.accessToken();
	}
	
	@At
	@Ok("raw")
	public String menu() {
		return WechatHandler.menu();
	}
	
	@At
	@Ok("raw")
	public String userOpenIds() {
		return WechatHandler.userOpenIds();
	}
	
	@At
	@Ok("raw")
	public String user(String openId) {
		return WechatHandler.user(openId);
	}
	
	@At
	@Ok("raw")
	public String users(String openIds) {
		if (Strings.isNotBlank(openIds)) {
			return WechatHandler.users(openIds.split(","));
		} else {
			String ids = userOpenIds();
			Pattern pattern = Pattern.compile("\\[.*\\]");
			Matcher matcher = pattern.matcher(ids);
			if(matcher.find()) {
				ids = matcher.group(0);
				ids = ids.replace("\"", "").replace("[", "").replace("]", "");
				return WechatHandler.users(ids.split(","));
			}
			return "";
		}
	}
	
	@At
	@Ok("raw")
	public String groups() {
		return WechatHandler.groups();
	}
	
	@At
	@Ok("raw")
	public String groupCreate(String name) {
		return WechatHandler.groupCreate(name);
	}
	
	@At
	@Ok("raw")
	public String groupDelete(String groupId) {
		return WechatHandler.groupDelete(groupId);
	}
	
	@At
	@Ok("raw")
	public String tags() {
		return WechatHandler.tags();
	}
	
	@At
	@Ok("raw")
	public String tagCreate(String name) {
		return WechatHandler.tagCreate(name);
	}
	
	@At
	@Ok("raw")
	public String tagDelete(String tagId) {
		return WechatHandler.tagDelete(tagId);
	}
	
	@At
	@Ok("raw")
	public String tagsOnUser(String openId) {
		return WechatHandler.tagsOnUser(openId);
	}
	
	@At
	@Ok("raw")
	public String usersInTag(String openId) {
		return WechatHandler.userOpenIdsInTag(openId, null);
	}
	
	@At
	@Ok("raw")
	public String usersAddTag(String openIds, String tagId) {
		return WechatHandler.addTagToUsers(openIds, tagId);
	}
	
	@At
	@Ok("raw")
	public String sendTplMsg_ActJoinSuccess(String openId, String tpId, String name, String date, String time, String address, String content) {
		TemplatelMsg msg = new TemplatelMsg(openId, tpId, "http://www.muchon.cn");
		msg.getData().put("name", new TemplateMsgDataProp(name, "#173177"));
		msg.getData().put("date", new TemplateMsgDataProp(date, "#173177"));
		msg.getData().put("time", new TemplateMsgDataProp(time, "#173177"));
		msg.getData().put("address", new TemplateMsgDataProp(address, "#173177"));
		msg.getData().put("content", new TemplateMsgDataProp(content, "#173177"));

		return WechatHandler.setTemplateMsg(msg);
	}
	
	@At
	@Ok("raw")
	public String sendTxtMsg(Boolean isToAll, int tagId, String content) {
		return WechatHandler.sendTextMsg(isToAll, tagId, content);
	}
	
	@At
	@Ok("raw")
	public String sendAtcMsg() {
		return App.hostUrl();
	}
	
	/**
	 * 替换图文信息内容中的图片地址为微信图片的地址（上传图片至微信）
	 */
	private String updateContent(String content) {
		String result = new String(content);
		String regEx = "src=[\\\",\\']([\\s\\S]*?[\\.jpg,\\.png,])[\\\",\\']\\s*"; //定义Image标签的正则表达式 
	         
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE); 
        Matcher m = p.matcher(content); 
        while(m.find()) {
        	String oldUrl = m.group(1);
        	String newUrl = oldUrl;
        	if (!oldUrl.toLowerCase().contains("http://")) { // 相对URL改为绝对URL，否则微信访问不到
        		oldUrl = App.hostUrl() + oldUrl;
        	}
        	// 先在数据库中查找
        	Attachment img = attachmentService.findByLocalUrl(oldUrl);
        	if (img != null) {
        		newUrl = img.getWechatUrl();
        	} else {
        		// 上传至微信
        		newUrl = WechatHandler.uploadImgInArticle(oldUrl);
        		// 保存至数据库
        		img = new Attachment();
        		img.setLocalUrl(oldUrl);
        		img.setWechatUrl(newUrl);
        		attachmentService.insert(img);
        	}
        	result = result.replace(m.group(1), newUrl);
        }
        
		return result;
	}
}
