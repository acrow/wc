package ratekey.wechat.app;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;

import ratekey.wechat.app.domain.Dict;

public abstract class App {
    private static Log log = Logs.get();

    private static boolean inited = false;

    private static NutConfig cfg;
    private static Properties prop;
    private static Properties up = new Properties();
    private static Properties weixin = new Properties();
    
    /** 系统中所有实现了Flowable接口的类 */
    private static List<Class<?>> flowables;
    
    /** 应用启动时间 */
    private static Date startupTime;

    /** 应用启动时间串 */
    private static String ver;

    /** 应用启动时间串 */
    private static String build;
    
    private static final String SALT = "Wx.root.2012";
    private static final String SHA1 = "SHA-1";
    private static final String UP = "up.properties";
    private static final String WEIXIN = "weixin.properties";
    private static final String RT = "rt";
    private static final String ROOT = "root";
    private static final String XMAN = "xman";

    public static final List<Dict> _articleTypes = new ArrayList<Dict>();
    public static final List<Dict> _staffGroups = new ArrayList<Dict>();
    public static Thread _weixinDomsgThread = null;
    
//    static Scheduler scheduler = null;
//    
//    public static Scheduler getScheduler(){
//        return scheduler;
//    }

    /** 初始化应用相关设置 */
    public static void init(NutConfig config) {
        if (inited) {
            return;
        }
        log.info("开始初始化App...");
        cfg = config;
        prop = cfg.getIoc().get(PropertiesProxy.class, "config").toProperties();
//        try {
//            up.load(Streams.fileIn(UP));
//            weixin.load(Streams.fileIn(WEIXIN));
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

//        Initializers.doInit();

        log.info("App初始化完毕！");
        startupTime = new Date(System.currentTimeMillis());
        ver = Times.format(new SimpleDateFormat("yyyyMMddHHmmss"), startupTime);
        build = Times.format(new SimpleDateFormat("yyyy-MM-dd"), startupTime);
        inited = true;
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
     * 没有用户时候默认管理员的口令
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
     * 获取配置了的法定节假日
     * @return
     */
    public static String getHolidays() {
        return prop.getProperty("app.quartz.holidays");
    }
    
    public static String getWorkdays() {
        return prop.getProperty("app.quartz.workdays");
    }
    
    /**
     * 获取工作时间的上午开始时间
     * @return
     */
    public static String getAmBegin() {
        return prop.getProperty("app.quartz.am.begin", "08:00");
    }
    
    /**
     * 获取工作时间的上午结束，默认为12:00
     * @return
     */
    public static String getAmEnd() {
        return prop.getProperty("app.quartz.am.end", "12:00");
    }
    
    /**
     * 获取工作时间的下午开始时间，默认为14:00
     * @return
     */
    public static String getPmBegin() {
        return prop.getProperty("app.quartz.pm.begin", "14:00");
    }

    /**
     * 获取工作时间的下午结束时间，默认为18:00
     * @return
     */
    public static String getPmEnd() {
        return prop.getProperty("app.quartz.pm.end", "18:00");
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
    
    /**
     * 是否允许执行quartz的调度任务
     * @return
     */
    public static boolean isEnableScheduler() {
        return Boolean.parseBoolean(prop.getProperty("app.isEnableScheduler", "false"));
    }

    
    /*
     * 当前的版本号
     */
    public static String ver(){
        return prop.getProperty("app.ver", ver);
    }
    
    public static String build(){
        return prop.getProperty("app.build", build);
    }

    public static List<Class<?>> getFlowables() {
        return flowables;
    }
    
    public static String weinxinToken(){
        return weixin.getProperty("weixin.Token", "xcrow2014");
    }
    
    public static String weixinAppID(){
        return weixin.getProperty("weixin.AppID", "wxdd46622ce2a78fee");
    }
    
    public static String weixinAppsecret(){
        return weixin.getProperty("weixin.Appsecret", "a44d6acbc8c4185a6c775bffc7059422");
    }
    
    public static String weixinAccessToken(){
        return weixin.getProperty("weixin.AccessToken", "");
    }
    
    public static String weixinArticleUrl(){
        return weixin.getProperty("weixin.ArticleUrl", "");
    }
    
    public static String weixinServerUrl(){
        return weixin.getProperty("weixin.ServerUrl", "");
    }
    
//    public static Date weixinAccessTokenTimestamp(){
//    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//			return  formatDate.parse(weixin.getProperty("weixin.AccessTokenTimestamp", "2014/01/01 00:00:01"));
//		} catch (ParseException e) {
//			Calendar cld = Calendar.getInstance();
//			cld.set(2014, 1,1);
//			return cld.getTime();
//		}
//    }
//    
//    /***
//     * 保存取得的微信Token和取得时间
//     * @param token
//     * @param tokenTime
//     * @return
//     */
//    public static boolean saveAccessToken(String token, Date tokenTime) {
//    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	weixin.setProperty("weixin.AccessToken", token);
//    	weixin.setProperty("weixin.AccessTokenTimestamp", formatDate.format(tokenTime));
//    	// 发现保存到物理文件会导致应用重启，所以先不要保存至物理文件
////        try {
////        	weixin.store(Streams.fileOut(WEIXIN), "");
////        }
////        catch (IOException e) {
////            e.printStackTrace();
////            return false;
////        }
//        return true;
//    }
//    
//    public static String getArticleTypeName(String typeId) {
//    	if (_articleTypes.size() == 0) {
//    		Ioc ioc = Webs.ioc();
//            Dao dao = ioc.get(Dao.class, "dao");
//            _articleTypes.addAll(dao.query(Dict.class, Cnd.where("Type", "=", "文章分类")));
//    	}
//		if (_articleTypes.size() > 0) {
//			Long id = Long.parseLong(typeId);
//			for(Dict d : _articleTypes) {
//				if (d.getId() == id) {
//					return d.getName();
//				}
//			}
//		}
//		return "基础知识";
//	}
//    
//    public static String getStaffGroupName(String groupId) {
//    	if (_staffGroups.size() == 0) {
//    		Ioc ioc = Webs.ioc();
//            Dao dao = ioc.get(Dao.class, "dao");
//            _staffGroups.addAll(dao.query(Dict.class, Cnd.where("Type", "=", "人员分组")));
//    	}
//		if (_staffGroups.size() > 0) {
//			Long id = Long.parseLong(groupId);
//			for(Dict d : _staffGroups) {
//				if (d.getId() == id) {
//					return d.getName();
//				}
//			}
//		}
//		return "其他组织";
//	}
}
