package muchon.wechat.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import muchon.wechat.base.domain.Constable;
import muchon.wechat.base.domain.IdEntity;

/***
 *  Value Object
 * 
 * @author ZCC
 * 
 */
@Table("ExpenseAccount")
public class ExpenseAccount  implements Serializable, IdEntity, Constable {

    private static final long serialVersionUID = -948878401540932335L;
    
    public static int STATE_BORROW = -1;
    public static int STATE_REIMBURSE = 1;
    public static int STATE_BOTH = 0;
    
    @Id
    private Integer id; // Id
    @Column
    private Date inputDate; // 录入日期
    @Column
    private String deptCode; // 部门编号
    @Column
    private String financeProjCode; // 项目财务编号
    @Column
    private String subject; // 科目
    @Column
    private String summary; // 摘要
    @Column
    private BigDecimal amount; // 支出金额
    @Column
    private String paymethod; // 支出方式
    @Column
    private String currencyType; // 币种
    @Column
    private String toCompany; // 发票单位
    @Column
    private String projManager; // 课题负责人
    @Column
    private String projCode; // 实际项目编号
    @Column
    private String submitter; // 报销人
    @Column
    private String inputer; // 录入人
    @Column
    private String result; // 处理结果
    @Column
    private String memo; // 备注
    @Column
    private String borrower; // 借款人
    @Column
    private Date borrowDate; // 借款日期
    @Column
    private Integer state; // 状态
    @Column
    private String accountSet; //账套
    @Column
    private Integer inputerId; // 录入人ID

    /***
     * 取得Id
     * @return
     */
    public Integer getId() {
        return id;
    }
    /***
     * 设置Id
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /***
     * 取得录入日期
     * @return
     */
    public Date getInputDate() {
        return inputDate;
    }
    /***
     * 设置录入日期
     * @param inputDate
     */
    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    /***
     * 取得部门编号
     * @return
     */
    public String getDeptCode() {
        return deptCode;
    }
    /***
     * 设置部门编号
     * @param deptCode
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /***
     * 取得项目财务编号
     * @return
     */
    public String getFinanceProjCode() {
        return financeProjCode;
    }
    /***
     * 设置项目财务编号
     * @param financeProjCode
     */
    public void setFinanceProjCode(String financeProjCode) {
        this.financeProjCode = financeProjCode;
    }

    /***
     * 取得科目
     * @return
     */
    public String getSubject() {
        return subject;
    }
    /***
     * 设置科目
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /***
     * 取得摘要
     * @return
     */
    public String getSummary() {
        return summary;
    }
    /***
     * 设置摘要
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /***
     * 取得支出金额
     * @return
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /***
     * 设置支出金额
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /***
     * 取得指出方式
     * @return
     */
    public String getPaymethod() {
        return paymethod;
    }
    /***
     * 设置指出方式
     * @param paymethod
     */
    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    /***
     * 取得币种
     * @return
     */
    public String getCurrencyType() {
        return currencyType;
    }
    /***
     * 设置币种
     * @param currencyType
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /***
     * 取得发票单位
     * @return
     */
    public String getToCompany() {
        return toCompany;
    }
    /***
     * 设置发票单位
     * @param toCompany
     */
    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    /***
     * 取得课题负责人
     * @return
     */
    public String getProjManager() {
        return projManager;
    }
    /***
     * 设置课题负责人
     * @param projManager
     */
    public void setProjManager(String projManager) {
        this.projManager = projManager;
    }

    /***
     * 取得实际项目编号
     * @return
     */
    public String getProjCode() {
        return projCode;
    }
    /***
     * 设置实际项目编号
     * @param projCode
     */
    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }

    /***
     * 取得报销人
     * @return
     */
    public String getSubmitter() {
        return submitter;
    }
    /***
     * 设置报销人
     * @param submitter
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    /***
     * 取得录入人
     * @return
     */
    public String getInputer() {
        return inputer;
    }
    /***
     * 设置录入人
     * @param inputer
     */
    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    /***
     * 取得处理结果
     * @return
     */
    public String getResult() {
        return result;
    }
    /***
     * 设置处理结果
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /***
     * 取得备注
     * @return
     */
    public String getMemo() {
        return memo;
    }
    /***
     * 设置备注
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /***
     * 取得借款人
     * @return
     */
    public String getBorrower() {
        return borrower;
    }
    /***
     * 设置借款人
     * @param borrower
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /***
     * 取得借款日期
     * @return
     */
    public Date getBorrowDate() {
        return borrowDate;
    }
    /***
     * 设置借款日期
     * @param borrowDate
     */
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    /***
     * 取得状态
     * @return
     */
    public Integer getState() {
        return state;
    }
    /***
     * 设置状态
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /***
     * 取得账套
     * @return
     */
 	public String getAccountSet() {
		return accountSet;
	}
    /***
     * 设置账套
     * @param accountSet
     */
	public void setAccountSet(String accountSet) {
		this.accountSet = accountSet;
	}
	
    /***
     * 取得录入人ID
     * @return
     */
	public Integer getInputerId() {
		return inputerId;
	}
    /***
     * 设置录入人ID
     * @param inputerId
     */
	public void setInputerId(Integer inputerId) {
		this.inputerId = inputerId;
	}
	
	
	@Override
 	public Map<String, String> getConstFieldMap() {
 		Map<String, String> fm = new HashMap<String, String>();
 		return fm;
 	}
 	@Override
 	public void wrap() {
 		// TODO Auto-generated method stub
 		
 	}
}