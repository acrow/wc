package muchon.wechat.app.service;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

import muchon.wechat.app.domain.Attachment;
import muchon.wechat.base.service.IdService;

@IocBean(fields = "dao")
public class AttachmentService extends IdService<Attachment> {
	public Attachment findByLocalUrl(String localUrl) {
		Cnd cnd = Cnd.where("localUrl", "=", localUrl);
		return fetch(cnd);
	}
}
