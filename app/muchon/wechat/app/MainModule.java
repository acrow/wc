package muchon.wechat.app;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import muchon.wechat.app.domain.Staff;

@Ok("json")
@Fail("json")
@Localization("msg")
@SetupBy(AppSetup.class)
@Modules(scanPackage = true)
@Encoding(input = "utf8", output = "utf8")
@IocBy(type = ComboIocProvider.class, args = {"*org.nutz.ioc.loader.json.JsonLoader",
                                              "ioc/",
                                              "*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
                                              "muchon.wechat"})
//@Filters({@By(type = CheckSession.class, args = {Const.USER_SESSION_KEY, "/login"}), @By(type = ShiroActionFilter.class, args = {"jsp:/login"})})
//@Filters(@By(type = CheckSession.class, args = {Staff.SESSION_KEY, "/login"}))
public class MainModule {
    @At("/")
    @Ok("->:/login")
    public void index() {

    }
}
