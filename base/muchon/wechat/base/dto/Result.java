package muchon.wechat.base.dto;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private boolean success;
    private String msg;
    private Object data;
    private Map<String, Object> attr = new HashMap<String, Object>();

    public static Result ok() {
        return ok(null, null);
    }

    public static Result ok(String msg) {
        return ok(msg, null);
    }

    public static Result ok(String msg, Object data) {
        Result r = new Result();
        r.setSuccess(true);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result err() {
        return err(null, null);
    }

    public static Result err(String msg) {
        return err(msg, null);
    }

    public static Result err(String msg, Object data) {
        Result r = new Result();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
    
    public Result addAttr(String key, Object value){
        this.attr.put(key, value);
        return this;
    }


    // =========================================================================
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
    
    
}