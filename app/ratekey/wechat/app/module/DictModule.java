package ratekey.wechat.app.module;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import ratekey.wechat.app.domain.Dict;
import ratekey.wechat.app.service.DictService;
import ratekey.wechat.base.dto.DataGrid;
import ratekey.wechat.base.dto.Result;

@IocBean
@At("/dict")
public class DictModule {
	
	private static final Log log = Logs.get();

	@Inject
	private DictService dictService;
	
	@At("/")
	@Ok("json")
	public Dict get(Integer id) {
		return  dictService.fetch(id);
	}
	@At("/")
	@POST
	@Ok("json")
	@AdaptBy(type=JsonAdaptor.class)
	public Result save(Dict dict) {
		return dictService.save(dict, null, null);
	}
	@At("/")
	@DELETE
	@Ok("json")
	public Result delete(Integer id) {
		return dictService.delete(id, null, null);
	}
	@At
	@Ok("json")
	public List<Dict> options(String type) {
		return dictService.query(Cnd.where("type", "=", type), null);
	}
	@At
	@Ok("json")
	public DataGrid all(String type) {
		return dictService.datagrid(Cnd.where("type", "=", type), null);
	}
}
