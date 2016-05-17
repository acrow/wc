package muchon.wechat.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;

public abstract class Context {
    private static Log log = Logs.get();

    private static boolean inited = false;

    private static NutConfig cfg;
    private static Properties prop;
    
    /** 应用启动时间 */
    private static Date startupTime;

    /** 应用启动时间串 */
    private static String ver;

    /** 应用启动时间串 */
    private static String build;
    

    /** 初始化应用相关设置 */
    public static void init(NutConfig config) {
        if (inited) {
            return;
        }
        Stopwatch sw = Stopwatch.begin();
        log.info("开始加载配置...");
        cfg = config;
        prop = cfg.getIoc().get(PropertiesProxy.class, "config").toProperties();

        log.info("加载配置完毕！");
        
        startupTime = new Date(System.currentTimeMillis());
        ver = Times.format(new SimpleDateFormat("yyyyMMddHHmmss"), startupTime);
        build = Times.format(new SimpleDateFormat("yyyy-MM-dd"), startupTime);
        inited = true;
        
        sw.stop();
        log.info("应用程序：" + getAppName());
        log.info("版本号：" + ver() + " 发布日期：" + build());
        log.info("启动用时：" + sw.getDuration());
    }
    
    /**
     * 获取配置NutConfig信息
     * 
     * @return
     */
    public static NutConfig getConfig() {
        return cfg;
    }

    /**
     * 当前应用的根路径，末尾没有/
     * 
     * @return
     */
    public static String getAppRoot() {
        return cfg.getAppRoot();
    }
    
    /**
     * 当前应用的class路径，末尾没有/
     * @return
     */
    public static String getClassRoot(){
    	return getAppRoot() + "/WEB-INF/classes";
    }

    /**
     * 当前应用的主页
     * 
     * @return
     */
    public static String getHomepage() {
        return prop.getProperty("app.homepage", "/index");
    }
    
    /**
     * 审批的主页
     * 
     * @return
     */
    public static String getAuditpage() {
        return prop.getProperty("app.auditpage", "/audit");
    }
    
    /**
     * 当前应用的名称
     * 
     * @return
     */
    public static String getAppName() {
        return prop.getProperty("app.name", "MyApp");
    }

    /**
     * 没有用户时候默认管理员的登录名
     * 
     * @return
     */
    public static String getAdminName() {
        return prop.getProperty("app.admin.name", "admin");
    }

    /**
     * 没有用户时候默认管理员的口令
     * 
     * @return
     */
    public static String getAdminPassword() {
        return prop.getProperty("app.admin.password", "admin");
    }
    
    /**
     * 添加用户时候默认的口令
     * 
     * @return
     */
    public static String getDefaultPassword() {
        return prop.getProperty("app.defalut.password", "wmc123");
    }
    
    /**
     * 超级模拟用户的默认岗位
     * @return
     */
    public static Long getXmanPosId() {
        return Long.parseLong(prop.getProperty("app.xman.posid", "626"));
    }

    /**
     * 是否自动创建数据库表
     * 
     * @return
     */
    public static boolean isAutoCreatTable() {
        return Boolean.parseBoolean(prop.getProperty("app.isAutoCreateTable", "false"));
    }

    /**
     * 是否强制创建数据库表
     * 
     * @return
     */
    public static boolean isForceCreatTable() {
        return Boolean.parseBoolean(prop.getProperty("app.isForceCreateTable", "false"));
    }

    /**
     * 是否开发模式
     * 
     * @return
     */
    public static boolean isDev() {
        return Boolean.parseBoolean(prop.getProperty("app.isDev", "false"));
    }

    
    /*
     * 当前的版本号
     */
    public static String ver(){
        return prop.getProperty("app.ver", ver);
    }
    
    /*
     * 发布时间
     */
    public static String build(){
        return prop.getProperty("app.build", build);
    }

    private static String _wechatAppId = "";
    public static void setWechatAppId(String wechatAppId) {
    	_wechatAppId = wechatAppId;
    }
    public static String getWechatAppId() {
    	if (Strings.isBlank(_wechatAppId)) {
    		_wechatAppId = prop.getProperty("wechat.appid", "");
    	}
    	return _wechatAppId;
    }
    
    private static String _wechatAppSecret = "";
    public static void setWechatAppSecret(String wechatAppSecret) {
    	_wechatAppSecret = wechatAppSecret;
    }
    public static String getWechatAppSecret() {
    	if (Strings.isBlank(_wechatAppSecret)) {
    		_wechatAppSecret = prop.getProperty("wechat.appsecret", "");
    	}
    	return _wechatAppSecret;
    }
    
    private static String _hostUrl = "";
    public static void setHostUrl(String hostUrl) {
    	 _hostUrl = hostUrl;
    }
    public static String getHostUrl() {
    	if (Strings.isBlank(_hostUrl)) {
    		_hostUrl = prop.getProperty("app.hosturl", "");
    	}
    	return _hostUrl;
    }
    
}
