package ratekey.wechat.app.service;

import org.nutz.ioc.loader.annotation.IocBean;

import ratekey.wechat.app.domain.Staff;
import ratekey.wechat.base.service.IdService;

@IocBean(fields = "dao")
public class StaffService extends IdService<Staff> {

}
