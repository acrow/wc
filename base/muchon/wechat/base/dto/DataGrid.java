package muchon.wechat.base.dto;

import java.io.Serializable;
import java.util.Collections;

public class DataGrid implements Serializable {
    private static final long serialVersionUID = 5548306071108136095L;
    
    /** 空的数据表格 */
    public static final DataGrid EMPTY = new DataGrid(0, Collections.EMPTY_LIST);

    public DataGrid() {

    }

    public DataGrid(Integer total, Object rows) {
        this(total, rows, null);
    }

    public DataGrid(Integer total, Object rows, Object footer) {
        this.rows = rows;
        this.total = total;
        this.footer = footer;
    }

    /**
     * 总共多少行，用于分页
     */
    private Integer total;
    /**
     * 表中主体数据结果，一般为一个集合
     */
    private Object rows;

    /**
     * 表的页脚，用于显示合计等信息
     */
    private Object footer;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public Object getFooter() {
        return footer;
    }

    public void setFooter(Object footer) {
        this.footer = footer;
    }

}
