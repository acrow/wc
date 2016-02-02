package ratekey.wechat.app.module;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import ratekey.wechat.app.domain.ExpenseAccount;
import ratekey.wechat.app.domain.Staff;
import ratekey.wechat.app.service.DictService;
import ratekey.wechat.app.service.ExpenseAccountService;
import ratekey.wechat.base.dto.DataGrid;
import ratekey.wechat.base.dto.Result;

@IocBean
@At("/expenseAccount")
public class ExpenseAccountModule {
	
	private static final Log log = Logs.get();

	@Inject
	private ExpenseAccountService expenseAccountService;
	@Inject
	private DictService dictService;
	
	/**
	 * 取得报销记录
	 * @param id
	 * @return
	 */
	@At("/")
	@GET
	@Ok("json")
	public ExpenseAccount get(Integer id) {
		return  expenseAccountService.fetch(id);
	}
	
	/**
	 * 保存（新建/更新）报销记录
	 * @param expenseAccount
	 * @return
	 */
	@At("/")
	@POST
	@Ok("json")
	@AdaptBy(type=JsonAdaptor.class)
	public Result save(ExpenseAccount expenseAccount, HttpSession session) {
		if (session.getAttribute(Staff.SESSION_KEY) != null) {
			Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
			expenseAccount.setInputer(usr.getName());
			expenseAccount.setInputerId(usr.getId());
		}
		Calendar cld = Calendar.getInstance();
		// 借款单的话给借款时间赋值
		if (expenseAccount.getState() == ExpenseAccount.STATE_BORROW) {
			if (expenseAccount.getBorrowDate() == null) {
				expenseAccount.setBorrowDate(cld.getTime());
			}
		} else { // 非借款单（包括对借款单冲账）给录入时间赋值
			if (expenseAccount.getInputDate() == null) {
				expenseAccount.setInputDate(cld.getTime());
			}
		}
		return expenseAccountService.save(expenseAccount, null, null);
	}
	
	/**
	 * 删除报销记录
	 * @param id
	 * @return
	 */
	@At("/")
	@DELETE
	@Ok("json")
	public Result delete(Integer id) {
		return expenseAccountService.delete(id, null, null);
	}
	
	@At
	@Ok("json")
	public DataGrid all(HttpSession session) {
		if (session.getAttribute(Staff.SESSION_KEY) == null) {
			return DataGrid.EMPTY;
		}
		Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
		// 只有管理员可以看到所有记录
		if (!"管理员".equals(usr.getRole())) {
			return DataGrid.EMPTY;
		}
		DataGrid datagrid= expenseAccountService.datagrid();
		return datagrid;
	}
	
	@At
	@Ok("json")
	public DataGrid query(ExpenseAccount entity, HttpSession session) {
		if (session.getAttribute(Staff.SESSION_KEY) == null) {
			return DataGrid.EMPTY;
		}
		Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
		// 管理员可以看所有人的，非管理员只能看到自己的
		if (!"管理员".equals(usr.getRole())) {
			if (entity == null) {
				entity = new ExpenseAccount();
			}
			entity.setInputerId(usr.getId());
		}
		
		Cnd cnd = null;
		if (entity != null) {
			if (entity.getState() != null) {
				if (cnd == null) {
					cnd = Cnd.where("state", "in", entity.getState() + ", " + ExpenseAccount.STATE_BOTH); // 已报销的既是报销记录又是借款记录
				} 
			}
			
			if (entity.getInputerId() != null) {
				if (cnd == null) {
					cnd = Cnd.where("inputerId", "=", entity.getInputerId());
				} else {
					cnd = cnd.and("inputerId", "=", entity.getInputerId());
				}
			}
			
			if (entity.getInputDate() != null) {
				if (cnd == null) {
					cnd = Cnd.where("inputDate", "=", entity.getInputDate());
				} else {
					cnd = cnd.and("inputDate", "=", entity.getInputDate());
				}
			}
			
			if (!Strings.isBlank(entity.getDeptCode())) {
				if (cnd == null) {
					cnd = Cnd.where("deptCode", "=", entity.getDeptCode());
				} else {
					cnd = cnd.and("deptCode", "=", entity.getDeptCode());
				}
			}
			
			if (!Strings.isBlank(entity.getFinanceProjCode())) {
				if (cnd == null) {
					cnd = Cnd.where("financeProjCode", "=", entity.getFinanceProjCode());
				} else {
					cnd = cnd.and("financeProjCode", "=", entity.getFinanceProjCode());
				}
			}
			
			if (!Strings.isBlank(entity.getSubject())) {
				if (cnd == null) {
					cnd = Cnd.where("subject", "=", entity.getSubject());
				} else {
					cnd = cnd.and("subject", "=", entity.getSubject());
				}
			}
			
			if (!Strings.isBlank(entity.getSummary())) {
				if (cnd == null) {
					cnd = Cnd.where("summary", "=", entity.getSummary());
				} else {
					cnd = cnd.and("summary", "=", entity.getSummary());
				}
			}
			
			if (entity.getAmount() != null) {
				if (cnd == null) {
					cnd = Cnd.where("amount", "=", entity.getAmount());
				} else {
					cnd = cnd.and("amount", "=", entity.getAmount());
				}
			}
			
			if (!Strings.isBlank(entity.getPaymethod())) {
				if (cnd == null) {
					cnd = Cnd.where("paymethod", "=", entity.getPaymethod());
				} else {
					cnd = cnd.and("paymethod", "=", entity.getPaymethod());
				}
			}
			
			if (!Strings.isBlank(entity.getCurrencyType())) {
				if (cnd == null) {
					cnd = Cnd.where("currencyType", "=", entity.getCurrencyType());
				} else {
					cnd = cnd.and("currencyType", "=", entity.getCurrencyType());
				}
			}
			
			if (!Strings.isBlank(entity.getToCompany())) {
				if (cnd == null) {
					cnd = Cnd.where("toCompany", "=", entity.getToCompany());
				} else {
					cnd = cnd.and("toCompany", "=", entity.getToCompany());
				}
			}
			
			if (!Strings.isBlank(entity.getProjManager())) {
				if (cnd == null) {
					cnd = Cnd.where("projManager", "=", entity.getProjManager());
				} else {
					cnd = cnd.and("projManager", "=", entity.getProjManager());
				}
			}
			
			if (!Strings.isBlank(entity.getProjCode())) {
				if (cnd == null) {
					cnd = Cnd.where("projCode", "=", entity.getProjCode());
				} else {
					cnd = cnd.and("projCode", "=", entity.getProjCode());
				}
			}
			
			if (!Strings.isBlank(entity.getSubmitter())) {
				if (cnd == null) {
					cnd = Cnd.where("submitter", "=", entity.getSubmitter());
				} else {
					cnd = cnd.and("submitter", "=", entity.getSubmitter());
				}
			}
			
			if (!Strings.isBlank(entity.getInputer())) {
				if (cnd == null) {
					cnd = Cnd.where("inputer", "=", entity.getInputer());
				} else {
					cnd = cnd.and("inputer", "=", entity.getInputer());
				}
			}
			
			if (!Strings.isBlank(entity.getResult())) {
				if (cnd == null) {
					cnd = Cnd.where("result", "=", entity.getResult());
				} else {
					cnd = cnd.and("result", "=", entity.getResult());
				}
			}
			
			if (!Strings.isBlank(entity.getMemo())) {
				if (cnd == null) {
					cnd = Cnd.where("memo", "=", entity.getMemo());
				} else {
					cnd = cnd.and("memo", "=", entity.getMemo());
				}
			}
			
			if (!Strings.isBlank(entity.getBorrower())) {
				if (cnd == null) {
					cnd = Cnd.where("borrower", "=", entity.getBorrower());
				} else {
					cnd = cnd.and("borrower", "=", entity.getBorrower());
				}
			}
			
			if (entity.getBorrowDate() != null) {
				if (cnd == null) {
					cnd = Cnd.where("borrowDate", "=", entity.getBorrowDate());
				} else {
					cnd = cnd.and("borrowDate", "=", entity.getBorrowDate());
				}
			}
		}
		DataGrid datagrid = null;
		if (cnd != null) {
			datagrid = expenseAccountService.datagrid(cnd);
		} else {
			datagrid= expenseAccountService.datagrid();
		}
		return datagrid;
	}
	
	@At
	@Ok("jsp:/view/rpt/auditApply")
	public List<ExpenseAccount> reportAuditApply(String manager, HttpSession session) {
		List<ExpenseAccount> expenseAccounts = new ArrayList<ExpenseAccount>();
		Cnd cnd = Cnd.where("projManager", "=", manager);
		cnd.and("state", "in", "1, 0");
//
//		if (session.getAttribute(Staff.SESSION_KEY) == null) {
//			return expenseAccounts;
//		}
//
//		Staff usr = (Staff)session.getAttribute(Staff.SESSION_KEY);
//		// 管理员可以看所有人的，非管理员只能看到自己的
//		if (!"管理员".equals(usr.getRole())) {
//			cnd.and("inputerId", "=", usr.getId());
//		}
		
		expenseAccounts = expenseAccountService.query(cnd, null);
		
		return expenseAccounts;
	}
	
	@At
	@Ok("json")
	public DataGrid reportMonthManager(String month, HttpSession session) {

		return null;
	}
}
