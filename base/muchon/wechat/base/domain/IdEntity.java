package muchon.wechat.base.domain;

/**
 * 具有整形主键且主键名称为id的实体类
 * @author WenWu
 *
 */
public interface IdEntity {
    /**
     * 获取主键值
     * @return
     */
    public Integer getId();
}
