package muchon.wechat.base.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class WxHandler {
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
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + "wxdd46622ce2a78fee"
							+ "&secret=" + "1e5ba02ed46f630c99e9631403e67aad");
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
	public static String groupDelete(String id) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=" + accessToken(), "{\"group\":{\"id\":" + id + "}}");
		return result;
	}
	
	/***
	 * 重命名用户分组
	 * @return
	 */
	public static String groupRename(String id, String name) {
		String result = httpsPostJson("https://api.weixin.qq.com/cgi-bin/groups/update?access_token=" + accessToken(), "{\"group\":{\"id\":" + id + ",\"name\":\"" + name + "\"}}");
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
			log.error("访问微信服务连接超时，链接：" + requestUrl + "方法：" + requestMethod + "； 参数：" + parametersJson + "； 错误信息：" + ce.getMessage());
		} catch (Exception e) {
			log.error("访问微信服务发生错误，链接：" + requestUrl + "方法：" + requestMethod + "； 参数：" + parametersJson + "； 错误信息：" + e.getMessage());
		}
		return result;
	}
	
//	/***
//	 * 通用HTTPS GET方法（废弃）
//	 * @param url
//	 * @return
//	 */
//	public static String httpsGetJson(String url) {
//		Response res = Http.get(url);
//		if (res.isOK()) {
//			try {
//				StringBuilder tokenSb = new StringBuilder();
//				CharBuffer charBuffer = CharBuffer.allocate(1024);
//				int state = res.getReader("UTF-8").read(charBuffer);
//				while (state != -1) {
//					charBuffer.flip();
//					while (charBuffer.hasRemaining()) {
//						tokenSb.append(charBuffer.get());
//					}
//					charBuffer.clear();
//					state = res.getReader("UTF-8").read(charBuffer);
//				}
//				res.getReader().close();
//				return tokenSb.toString();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
}
