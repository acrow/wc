package muchon.wechat.app.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;

import muchon.wechat.app.domain.ExpenseAccount;
import muchon.wechat.base.service.IdService;

@IocBean(fields = "dao")
public class ExpenseAccountService extends IdService<ExpenseAccount> {
	
	public List<ExpenseAccount> query(String title, String type, Pager page) {
		Cnd cnd = Cnd.where("state", "=", 1);
//		String typeId = "";
//		if (Strings.isNotBlank(type)) {
//			for (Dict d : App._ExpenseAccountTypes) {
//				if (d.getName().equals(type)) {
//					typeId = d.getId().toString();
//					break;
//				}
//			}
//		}
//		if (Strings.isNotBlank(typeId)) {
//				cnd.and("type","=",  typeId);
//		}
//		if (Strings.isNotBlank(title)) {
//			cnd.and("title","like","%" + title + "%");
//		}
//		
//		page.setRecordCount(count(cnd));
//		
//		cnd.desc("createTime");
		List<ExpenseAccount> ExpenseAccounts = query(cnd, page);
//		for (ExpenseAccount a : ExpenseAccounts) {
//			a.setTypeName(App.getExpenseAccountTypeName(a.getType()));
//		}
		return ExpenseAccounts;
	}
}
