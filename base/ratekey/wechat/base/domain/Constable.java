package ratekey.wechat.base.domain;

import java.util.Map;

public interface Constable extends Wrapable{
    /**
     * 常量项到常量显示的映射
     * @return
     */
    public Map<String, String> getConstFieldMap();
}
