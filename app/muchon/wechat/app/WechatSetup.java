package muchon.wechat.app;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

public class WechatSetup implements Setup {
    private static Log log = Logs.get();

    @Override
    public void init(NutConfig config) {
        log.info("开始初始化应用...");
        Context.init(config);
//
//        Dao dao = Daos.dao();
//        ((NutDao)dao).setSqlManager(new FileSqlManager("sqls/"));
//        log.debug("Dao加载了" + dao.sqls().count() + "条SQL语句");
        
        log.info("应用初始化完毕！");
    }

    @Override
    public void destroy(NutConfig config) {
        log.info("开始销毁应用...");

        log.info("应用销毁完毕！");
    }

}
