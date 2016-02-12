package muchon.wechat.app.service;

import org.nutz.ioc.loader.annotation.IocBean;

import muchon.wechat.app.domain.Dict;
import muchon.wechat.base.service.IdService;

@IocBean(fields = "dao")
public class DictService extends IdService<Dict> {

}
