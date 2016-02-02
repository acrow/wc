package ratekey.wechat.app.service;

import org.nutz.ioc.loader.annotation.IocBean;

import ratekey.wechat.app.domain.Dict;
import ratekey.wechat.base.service.IdService;

@IocBean(fields = "dao")
public class DictService extends IdService<Dict> {

}
