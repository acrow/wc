package muchon.wechat.app;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

public class AppSetup implements Setup {
    private static Log log = Logs.get();

    @Override
    public void init(NutConfig config) {
        log.info("开始初始化AppSetup...");
        App.init(config);
//
//        Dao dao = Daos.dao();
//        ((NutDao)dao).setSqlManager(new FileSqlManager("sqls/"));
//        log.debug("Dao加载了" + dao.sqls().count() + "条SQL语句");
        
        log.info("AppSetup初始化完毕！");
    }

    @Override
    public void destroy(NutConfig config) {
        log.info("开始销毁AppSetup...");

        log.info("AppSetup销毁完毕！");
    }

}
