package muchon.wechat.base.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

import muchon.wechat.base.domain.IdEntity;
import muchon.wechat.base.domain.Wrapable;
import muchon.wechat.base.dto.DataGrid;
import muchon.wechat.base.dto.Result;
import muchon.wechat.base.util.Clazzs;
import muchon.wechat.base.util.Queries;

public abstract class IdService<T extends IdEntity> extends IdEntityService<T> {
    private static Log log = Logs.get();
    
    /**
     * 保存某个对象
     * 
     * @param obj
     *            要保存的对象
     * @param okMsg
     *            保存成功的提示
     * @param errMsg
     *            保存不成功的提示
     * @return
     */
    public Result save(T obj, String okMsg, String errMsg) {
        save(obj);
        Integer id = ((IdEntity) obj).getId();
        if (null != id) {
            return Result.ok(Strings.sBlank(okMsg, OK_SAVE)).addAttr(ID, id).addAttr(DATA, obj);
        } else {
            return Result.err(Strings.sBlank(errMsg, ERR_SAVE));
        }
    }

    /**
     * 新建某个对象
     * 
     * @param obj
     *            要新建的对象
     * @param okMsg
     *            新建成功的提示信息
     * @param errMsg
     *            新建失败的提示信息
     * @return
     */
    public Result insert(T obj, String okMsg, String errMsg) {
        insert(obj);
        Integer id = ((IdEntity) obj).getId();
        if (null != id) {
            return Result.ok(Strings.sBlank(okMsg, OK_INSERT)).addAttr(ID, id).addAttr(DATA, obj);
        } else {
            return Result.err(Strings.sBlank(errMsg, ERR_INSERT));
        }
    }

    /**
     * 更新某个对象
     * 
     * @param obj
     *            要更新的对象
     * @param okMsg
     *            更新成功的提示信息
     * @param errMsg
     *            更新失败的提示信息
     * @return
     */
    public Result update(T obj, String okMsg, String errMsg) {
        boolean success = update(obj);
        if (success) {
            Integer id = ((IdEntity) obj).getId();
            return Result.ok(Strings.sBlank(okMsg, OK_UPDATE)).addAttr(ID, id).addAttr(DATA, obj);
        } else {
            return Result.err(Strings.sBlank(errMsg, ERR_UPDATE));
        }
    }

    /**
     * 根据主键删除某个对象
     * 
     * @param id
     *            要删除对象的主键
     * @param okMsg
     *            删除成功的提示信息
     * @param errMsg
     *            删除失败的提示信息
     * @return
     */
    public Result delete(Integer id, String okMsg, String errMsg) {
        boolean success = delete(id);
        if (success) {
            return Result.ok(Strings.sBlank(okMsg, OK_DELETE));
        } else {
            return Result.ok(Strings.sBlank(errMsg, ERR_DELETE));
        }
    }
    
    /**
     * 根据主键删除一批对象
     * 
     * @param ids
     *            逗号分割的Long类型的主键
     * @param okMsg
     *            删除成功的提示信息
     * @param errMsg
     *            删除失败的提示信息
     * @return
     */
    public Result xdelete(String ids, String okMsg, String errMsg) {
        boolean success = xdelete(ids);
        if (success) {
            return Result.ok(Strings.sBlank(okMsg, OK_DELETE));
        } else {
            return Result.ok(Strings.sBlank(errMsg, ERR_DELETE));
        }
    }

    /**
     * 保存对象
     * 
     * @param obj
     * @return
     */
    public T save(T obj) {
        beforeSave(obj);
        if (null == obj.getId()) {
            insert(obj);
        } else {
            update(obj);
        }
        afterSave(obj);
        return obj;
    }

    /**
     * 保存前需要执行的动作，在 beforeInsert 与 beforeUpdate 前执行
     * 
     * @param obj
     */
    protected void beforeSave(T obj) {

    }

    /**
     * 保存后需要执行的动作，在 afterInsert 与 afterUpdate 后执行
     * 
     * @param obj
     */
    protected void afterSave(T obj) {

    }

    /**
     * 插入对象
     * 
     * @param obj
     * @return
     */
    public T insert(T obj) {
        beforeInsert(obj);
        dao().insert(obj);
        afterInsert(obj);
        return obj;
    }

    /**
     * 插入前需要执行的动作
     * 
     * @param obj
     */
    protected void beforeInsert(T obj) {

    }

    /**
     * 插入后需要执行的动作
     * 
     * @param obj
     */
    protected void afterInsert(T obj) {

    }

    /**
     * 更新对象
     * 
     * @param obj
     * @return
     */
    public boolean update(T obj) {
        beforeUpdate(obj);
        boolean success = (1 == dao().update(obj));
        if (success) {
            afterUpdate(obj);
        }
        return success;
    }

    /**
     * 更新前需要执行的动作
     * 
     * @param obj
     */
    protected void beforeUpdate(T obj) {

    }

    /**
     * 更新后需要执行的动作
     * 
     * @param obj
     */
    protected void afterUpdate(T obj) {

    }

    /**
     * 按主键删除某个记录
     * 
     * @param id
     * @return
     */
    public boolean delete(Integer id) {
        T obj = super.fetch(id);
        if (obj == null) {
            return false;
        }
        beforeDelete(obj);
        boolean success = (1 == super.delete(id));
        if (success) {
            afterDelete(obj);
        }
        return success;
    }
    
    /**
     * 批量删除记录
     * @param ids 使用逗号(,)分割的Long类型主键
     * @return
     */
    public boolean xdelete(String ids){
        if(Strings.isBlank(ids)){
            return false;
        }
        List<Long> lids = Lang.array2list(Strings.splitIgnoreBlank(ids, ","), Long.class);
        if(!lids.isEmpty()){
            dao().clear(getEntityClass(), Cnd.where("id", "in", lids));
            return true;
        }
        return false;
    }

    /**
     * 删除前执行
     * 
     * @param id
     */
    protected void beforeDelete(T obj) {

    }

    /**
     * 删除后执行
     * 
     * @param id
     */
    protected void afterDelete(T obj) {

    }

    /**
     * 按主键查看某个对象
     * 
     * @param id
     * @return
     */
    public T view(Long id) {
        T obj = this.fetch(id);
        afterView(obj);
        if (Clazzs.hasInterface(getEntityClass(), Wrapable.class)) {
            ((Wrapable)obj).wrap();
        }
        return obj;
    }

    /**
     * 获取到该查看对象后执行
     * 
     * @param obj
     */
    protected void afterView(T obj) {

    }

    // =========================================================================

    /**
     * 没有初始条件，使用实体类最快速的创建列表查询结果（自动根据实体对象与请求参数生成查询条件与排序方式）
     * 
     * @return
     */
    public DataGrid datagrid() {
        return datagrid(false);
    }
    
    /**
     * 使用给定的初始排序方式进行排序
     * @param sort
     * @param order
     * @return
     */
    public DataGrid datagrid(String sort, String order) {
    	return datagrid(false, sort, order);
    }

    /**
     * 没有初始条件，使用实体类最快速的创建列表查询结果（自动根据实体对象与请求参数生成查询条件与排序方式）
     * 
     * @param forcePage
     *            是否强制分页
     * @return
     */
    public DataGrid datagrid(boolean forcePage) {
        Pager pager = Queries.pager(forcePage);
        Cnd cnd = Queries.make(getEntityClass(), ID, DESC);
        return datagrid(cnd, pager);
    }
    
    /**
     * 没有初始条件，使用实体类最快速的创建列表查询结果
     * @param forcePage 是否强制分页
     * @param sort      排序字段
     * @param order     排序方式
     * @return
     */
    public DataGrid datagrid(boolean forcePage, String sort, String order) {
        Pager pager = Queries.pager(forcePage);
        Cnd cnd = Queries.make(getEntityClass(), sort, order);
        return datagrid(cnd, pager);
    }
    
    /**
     * 没有初始条件，使用实体类最快速的创建列表查询结果
     * @param forcePage 是否强制分页
     * @param sort      排序字段1
     * @param order     排序方式1
     * @param sort      排序字段2
     * @param order     排序方式2
     * @return
     */
    public DataGrid datagrid(boolean forcePage, String sort1, String order1, String sort2, String order2) {
        Pager pager = Queries.pager(forcePage);
        Cnd cnd = Queries.make(getEntityClass(), sort1, order1);
        if (!Strings.isBlank(sort2)) {
            if ("desc".equalsIgnoreCase(order2)) {
                cnd.desc(sort2);
            } else {
                cnd.asc(sort2);
            }
        }
        return datagrid(cnd, pager);
    }

    /**
     * 给定初始条件，快速创建列表查询结果，默认使用主键逆序排序（自动根据实体对象与请求参数生成查询条件与排序方式）
     * 
     * @param cnd
     * @return
     */
    public DataGrid datagrid(Cnd cnd) {
        return datagrid(false, cnd);
    }

    /**
     * 给定初始条件，快速创建列表查询结果，默认使用主键逆序排序（自动根据实体对象与请求参数生成查询条件与排序方式）
     * 
     * @param forcePage
     *            是否强制分页
     * @param cnd
     *            初始条件
     * @return
     */
    public DataGrid datagrid(boolean forcePage, Cnd cnd) {
        Pager pager = Queries.pager(forcePage);
        cnd = Queries.make(cnd, true, getEntityClass(), ID, DESC);
        return datagrid(cnd, pager);
    }

    /**
     * 给定初始条件与排序字段，快速创建列表查询结果（自动根据实体对象与请求参数生成查询条件与排序方式）
     * 
     * @param cnd
     *            初始条件
     * @param sort
     *            排序字段
     * @param order
     *            排序方式，asc 或 desc
     * @return
     */
    public DataGrid datagrid(Cnd cnd, String sort, String order) {
        Pager pager = Queries.pager();
        cnd = Queries.make(cnd, true, getEntityClass(), sort, order);
        return datagrid(cnd, pager);
    }

    /**
     * 给定查询条件与分页对象创建列表查询结果
     * 
     * @param cnd
     *            查询条件
     * @param pager
     *            分页对象
     * @return
     */
    public DataGrid datagrid(Cnd cnd, Pager pager) {
        int count = -1;
        List<T> items = query(cnd, pager);
        if (items !=null && items.size() > 0 && Clazzs.hasInterface(getEntityClass(), Wrapable.class)) {
            log.debug("wrap items..");
            for (T item : items) {
                ((Wrapable)item).wrap();
            }
        }
        if (null == pager) {
            count = items.size();
        } else {
            count = count(cnd);
            pager.setRecordCount(count);
        }
        return new DataGrid(count, items);
    }

    private static final String DATA = "data";
    
    private static final String ID = "id";
    private static final String DESC = "desc";

    private static final String OK_SAVE = "保存成功";
    private static final String OK_INSERT = "新建成功";
    private static final String OK_UPDATE = "更新成功";
    private static final String OK_DELETE = "删除成功";

    private static final String ERR_SAVE = "保存失败";
    private static final String ERR_INSERT = "新建失败";
    private static final String ERR_UPDATE = "更新失败";
    private static final String ERR_DELETE = "删除失败";
}
