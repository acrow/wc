package muchon.wechat.app.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import muchon.wechat.app.App;
import muchon.wechat.app.domain.wechat.ArticleMsg;
import muchon.wechat.app.domain.wechat.TemplatelMsg;
import muchon.wechat.base.dto.MyX509TrustManager;

public class WechatHandler {
	private static final Log log = Logs.get();
	
	/*************************************************************************/
	/*                               TOKEN管理                                                                                   */
	/*************************************************************************/
	private static String _accessToken = "";
	private static Date _accessTokenExpires = new Date();
	/***
	 * 获取微信ACCESS_TOKEN
	 * @return
	 */
	public static String accessToken() {
		if (Strings.isBlank(_accessToken) || (new Date()).after(_accessTokenExpires)) {
			
			Map<String, String> resultMap = httpsGetMap(
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + App.wechatAppId()
							+ "&secret=" + App.wechatAppSecret());
			if (resultMap != null) {
				_accessToken = resultMap.get("access_token");
				_accessTokenExpires = new Date(new Date().getTime() + (Long.parseLong(resultMap.get("expires_in")) - 200) * 1000);
			}
		}
		return _accessToken;
	}

	/*************************************************************************/
	/*                               菜单管理                                                                                      */
	/*************************************************************************/
	
	/***
	 * 获取微信菜单
	 * @return
	 */
	public static String menu() {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken());
		return result;
	}
	
	/***
	 * 创建微信菜单
	 * @return
	 */
	public static String menuCreate(String menuJson) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken(), menuJson);
		return result;
	}
	
	/***
	 * 创建微信个性化菜单
	 * @return
	 */
	public static String menuCreateConditional(String menuJson) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + accessToken(), menuJson);
		return result;
	}
	
	/***
	 * 销毁微信菜单（全部菜单）
	 * @return
	 */
	public static String menuDelete() {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken());
		return result;
	}
	
	/*************************************************************************/
	/*                               用户管理                                                                                      */
	/*************************************************************************/
	
	/***
	 * 获取微信用户OPENID列表（一次最多10000个）
	 * @param openId 参考用户ID（获取此用户ID后面的用户） 
	 * @return
	 */
	public static String userOpenIds(String openId) {
		String nextOpenId = "";
		if (!Strings.isBlank(openId)){
			nextOpenId = "&next_openid=" + openId;
		}
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken() + nextOpenId);
		return result;
	}
	
	/***
	 * 获取微信用户OPENID列表
	 * @return
	 */
	public static String userOpenIds() {
		return userOpenIds(null);
	}
	
	/***
	 * 获取用户详细信息
	 * @param openId 用户OPENID
	 * @return
	 */
	public static String user(String openId) {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken() + "&openid=" + openId + "&lang=zh_CN");
		return result;
	}
	
	/***
	 * 批量获取用户详细信息
	 * @param openIds 用户OPENID数组
	 * @return
	 */
	public static String users(String[] openIds) {
		String para = "{\"user_list\":[";
		int i=0;
		for(String openId : openIds) {
			if (i++>0) {
				para += ",";
			}
			para += "{\"openid\":\"" + openId + "\",\"lang\":\"zh-CN\"}"; 
		}
		para += "]}";

		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" + accessToken(), para);
		return result;
	}
	
	/***
	 * 获取用户所有分组
	 * @return
	 */
	public static String groups() {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/groups/get?access_token=" + accessToken());
		return result;
	}
	
	/***
	 * 创建用户分组
	 * @return
	 */
	public static String groupCreate(String name) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/create?access_token=" + accessToken(), "{\"group\":{\"name\":\"" + name + "\"}}");
		return result;
	}
	
	/***
	 * 销毁用户分组
	 * @return
	 */
	public static String groupDelete(String groupId) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=" + accessToken(), "{\"group\":{\"id\":" + groupId + "}}");
		return result;
	}
	
	/***
	 * 重命名用户分组
	 * @return
	 */
	public static String groupUpdate(String groupId, String name) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/update?access_token=" + accessToken(), "{\"group\":{\"id\":" + groupId + ",\"name\":\"" + name + "\"}}");
		return result;
	}
	
	/***
	 * 查询用户所在分组
	 * @return
	 */
	public static String queryGroup(String openId) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=" + accessToken(), "{\"openid\":\"" + openId + "\"}");
		return result;
	}
	
	/***
	 * 批量移动用户至分组
	 * @return
	 */
	public static String moveUsersToGroup(String openIds, String groupId) {
		openIds = "\"" + openIds.replace(",", "\",\"") + "\"";
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=" + accessToken(), "{\"openid_list\":[" + openIds + "],\"to_groupid\":" + groupId + "}");
		return result;
	}
	
	/***
	 * 获取所有标签
	 * @return
	 */
	public static String tags() {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + accessToken());
		return result;
	}
	
	/***
	 * 创建标签
	 * @return
	 */
	public static String tagCreate(String name) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/create?access_token=" + accessToken(), "{\"tag\":{\"name\":\"" + name + "\"}}");
		return result;
	}
	
	/***
	 * 编辑标签
	 * @return
	 */
	public static String tagUpdate(String tagId, String name) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/update?access_token=" + accessToken(), "{\"tag\":{\"id\":" + tagId + ",\"name\":\"" + name + "\"}}");
		return result;
	}
	
	/***
	 * 删除标签
	 * @return
	 */
	public static String tagDelete(String tagId) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=" + accessToken(), "{\"tagid\":" + tagId + "}");
		return result;
	}
	
	/***
	 * 获取标签下用户OPENID列表（一次最多10000个）
	 * @param openId 参考用户ID（获取此用户ID后面的用户） 
	 * @return
	 */
	public static String userOpenIdsInTag(String tagId, String openId) {
		String nextOpenId = "";
		if (Strings.isNotBlank(openId)){
			nextOpenId = "&next_openid=" + openId;
		}
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=" + accessToken() + nextOpenId);
		return result;
	}
	
	/***
	 * 获取用户身上的标签列表
	 * @return
	 */
	public static String tagsOnUser(String openId) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=" + accessToken(), "{\"openid\":\"" + openId + "\"}");
		return result;
	}
	
	/***
	 * 批量为用户打标签
	 * @return
	 */
	public static String addTagToUsers(String openIds, String tagId) {
	 	openIds = "\"" + openIds.replace(",", "\",\"") + "\"";
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + accessToken(), "{\"openid_list\":[" + openIds + "],\"tagid\":" + tagId + "}");
		return result;
	}
	
	/***
	 * 批量为用户删标签
	 * @return
	 */
	public static String delTagFromUsers(String openIds, String tagId) {
		openIds = "\"" + openIds.replace(",", "\",\"") + "\"";
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=" + accessToken(), "{\"openid_list\":[" + openIds + "],\"tagid\":" + tagId + "}");
		return result;
	}
	
	/*************************************************************************/
	/*                               模板消息                                                                                      */
	/*************************************************************************/
	
	/***
	 * 设置所属行业
	 * @return
	 */
	public static String setIndustry(String id1, String id2) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=" + accessToken(), "{\"industry_id1\":\"" + id1 + "\",\"industry_id2\":\"" + id2 + "\"}");
		return result;
	}
	
	/***
	 * 获取当前设置的行业
	 * @return
	 */
	public static String getIndustry() {
		String result = httpsGetJson("https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=" + accessToken());
		return result;
	}
	
	/***
	 * 发送模板消息
	 * @return
	 */
	public static String setTemplateMsg(TemplatelMsg msg) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken(), Json.toJson(msg));
		return result;
	}
	
	/*************************************************************************/
	/*                               图文消息                                                                                      */
	/*************************************************************************/
	
	/***
	 * 上传图文中的图片至微信
	 * @return
	 */
	public static String uploadImgInArticle(String imgPath) {
		String result = httpsUpload("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token==" + accessToken(), imgPath);
		return result;
	}
	
	/***
	 * 上传图文信息
	 * @param msg
	 * @return
	 */
	public static String uploadArticleMsg(ArticleMsg msg) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + accessToken(), Json.toJson(msg));
		return result;
	}
	
	/***
	 * 群发媒体型信息
	 * @param msg
	 * @return
	 */
	public static String sendMediaMsg(Boolean isToAll, int tagId, String mediaId, String msgType) {
		HashMap<String,Object> msg = new HashMap<String,Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("is_to_all", isToAll);
		filter.put("tag_id", tagId);
		Map<String, String> msgIdMap = new HashMap<String, String>();
		msgIdMap.put("media_id", mediaId);
		msg.put("filter", filter);
		msg.put(msgType, msgIdMap);
		msg.put("msgtype", msgType);
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + accessToken(), Json.toJson(msg));
		return result;
	}
	/***
	 * 群发图文消息
	 * @param isToAll
	 * @param tagId
	 * @param mediaId
	 * @return
	 */
	public static String sendArticleMsg(Boolean isToAll, int tagId, String mediaId) {
		return sendMediaMsg(isToAll, tagId, mediaId, "mpnews");
	}
	/***
	 * 群发语音消息
	 * @param isToAll
	 * @param tagId
	 * @param mediaId
	 * @return
	 */
	public static String sendVoiceMsg(Boolean isToAll, int tagId, String mediaId) {
		return sendMediaMsg(isToAll, tagId, mediaId, "voice");
	}
	/***
	 * 群发图片消息
	 * @param isToAll
	 * @param tagId
	 * @param mediaId
	 * @return
	 */
	public static String sendImgMsg(Boolean isToAll, int tagId, String mediaId) {
		return sendMediaMsg(isToAll, tagId, mediaId, "image");
	}
	/***
	 * 群发视频消息
	 * @param isToAll
	 * @param tagId
	 * @param mediaId(此处MediaId为加工后的meidaId)
	 * @return
	 */
	public static String sendVideoMsg(Boolean isToAll, int tagId, String mediaId) {
		return sendMediaMsg(isToAll, tagId, mediaId, "mpvideo");
	}
	/***
	 * 群发文本信息
	 * @param msg
	 * @return
	 */
	public static String sendTextMsg(Boolean isToAll, int tagId, String content) {
		HashMap<String,Object> msg = new HashMap<String,Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("is_to_all", isToAll);
		filter.put("tag_id", tagId);
		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("content", content);
		msg.put("filter", filter);
		msg.put("text", contentMap);
		msg.put("msgtype", "text");
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + accessToken(), Json.toJson(msg));
		return result;
	}
	/*************************************************************************/
	/*                               素材                                                                                      */
	/*************************************************************************/
	
	/***
	 * 上传图文中的图片至微信
	 * @return
	 */
	public static String uploadIgInArticle(String imgPath) {
		String result = httpsUpload("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token==" + accessToken(), imgPath);
		return result;
	}
	
	
	/*************************************************************************/
	/*                               通用方法                                                                                      */
	/*************************************************************************/
	
	/***
	 * 通用HTTPS GET方法
	 * @param url
	 * @return
	 */
	public static String httpsGetJson(String url) {
		return httpsRequest(url, "GET", null);
	}
	public static Map<String, String> httpsGetMap(String url) {
		String json = httpsGetJson(url);
		return Json.fromJsonAsMap(String.class, json);
	}
	
	/***
	 * 通用HTTPS POST方法
	 * @param url
	 * @return
	 */
	public static String httpsPostJson(String url, String parametersJson) {
		return httpsRequest(url, "POST", parametersJson);
	}
	public static Map<String, String> httpsPostMap(String url, String parametersJson) {
		String json = httpsPostJson(url, parametersJson);
		return Json.fromJsonAsMap(String.class, json);
	}
	
	/**
	 * 通用发送HTTPS请求
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param parametersJson 提交数据的JSON字符串
	 * @return String 
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String parametersJson) {
		String result = "";
		try {
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当提交数据不为null时向输出流写数据
			if (null != parametersJson) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(parametersJson.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			result = buffer.toString();
			// 如果返回的是错误，抛出异常
			if (result.startsWith("{\"err") && !result.startsWith("{\"errcode\":0")) {
				throw new Exception(result);
			}
		} catch (ConnectException ce) {
			log.error("访问微信服务连接超时，链接：" + requestUrl + "; 方法：" + requestMethod + "； 参数：" + parametersJson + "； 错误信息：" + ce.getMessage());
			ce.printStackTrace();
		} catch (Exception e) {
			log.error("访问微信服务发生错误，链接：" + requestUrl + "; 方法：" + requestMethod + "； 参数：" + parametersJson + "； 错误信息：" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Https方式上传文件
	 * @param uploadUrl
	 * @param filePath
	 * @return Json上传结果
	 * @throws Exception
	 */
	private static String httpsUpload(String uploadUrl, String filePath)  {
		System.out.println(uploadUrl);
		String result = null;
		try{
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			InputStream fileUrlInputStream = new URL(filePath).openStream();
			URL urlObj = new URL(uploadUrl);
			HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
			con.setSSLSocketFactory(ssf);
			con.setRequestMethod("POST");// 以POST方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);// POST方式不能使用缓存
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
					+ BOUNDARY);
			// 请求正文信息
			// 第一部分
			StringBuilder sb = new StringBuilder();
			sb.append("--");// 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
					+ filePath.substring(filePath.lastIndexOf("/") + 1) + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件以流文件的方式推入到url中
			DataInputStream in = new DataInputStream(fileUrlInputStream);
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			fileUrlInputStream.close();
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			try {
				// 定义BufferedReader输出流来读取URL的响应
				reader = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					// System.out.println(line);
					buffer.append(line);
				}
				if (result == null) {
					result = buffer.toString();
				}
			} catch (IOException e) {
				log.error("图文消息上传时发生错误，链接：" + uploadUrl + "; 文件路径：" + filePath + "；  错误信息：" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			log.error("图文消息上传时发生错误，链接：" + uploadUrl + "; 文件路径：" + filePath + "；  错误信息：" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
