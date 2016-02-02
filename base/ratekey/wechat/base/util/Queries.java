package ratekey.wechat.base.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.nutz.castor.Castors;
import org.nutz.castor.FailToCastObjectException;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

public abstract class Queries {
    /**
     * 获取分页信息
     * @return
     */
    public static Pager pager() {
        return pager(false);
    }

    /**
     * 获取分页信息
     * 
     * @param forcePage
     *            是否强制分页
     * @return
     */
    public static Pager pager(boolean forcePage) {
        Pager pager = null;
        HttpServletRequest req = Mvcs.getReq();
        String page = req.getParameter("page");
        String rows = req.getParameter("rows");
        if (!Strings.isBlank(page) && !Strings.isBlank(rows)) {
            pager = new Pager();
            pager.setPageNumber(Integer.parseInt(page));
            pager.setPageSize(Integer.parseInt(rows));
        }else if(forcePage){
            pager = new Pager();
            pager.setPageNumber(1);
            pager.setPageSize(50);
        }
        return pager;
    }
    
    /**
     * 按规则生成查询条件与排序条件
     * @param clazz        要生成条件的实体类
     * @return
     */
    public static Cnd make(Class<?> clazz){
        return make(clazz, null, null);
    }
    
    /**
     * 按规则生成查询条件与排序条件
     * @param clazz        要生成条件的实体类
     * @param defaultSort  无排序时默认字段
     * @return
     */
    public static Cnd make(Class<?> clazz, String defaultSort){
        return make(clazz, defaultSort, null);
    }

    /**
     * 按规则生成查询条件与排序条件
     * @param clazz        要生成条件的实体类
     * @param defaultSort  无排序时默认字段
     * @param defaultOrder 无排序时默认排序顺序
     * @return
     */
    public static Cnd make( Class<?> clazz, String defaultSort, String defaultOrder){
        return make(null, false, clazz, defaultSort, defaultOrder);
    }
    
    /**
     * 按规则生成查询条件与排序条件
     * @param cnd          初始条件
     * @param limited      是否受初始条件限制
     * @param clazz        要生成条件的实体类
     * @param defaultSort  无排序时默认字段
     * @param defaultOrder 无排序时默认排序顺序
     * @return
     */
    public static Cnd make(Cnd cnd, boolean limited, Class<?> clazz, String defaultSort, String defaultOrder){
        if (!Clazzs.hasAnnotation(clazz, Table.class)) {
            return null;
        }
        
        if(!limited || cnd == null){
            cnd = Cnd.limit();
        }

        HttpServletRequest req = Mvcs.getReq();

        @SuppressWarnings("unchecked")
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String fieldName = param;
            String optName = EQ;// 默认操作为等于
            String value = req.getParameter(param);
            if (isPrefixedParam(param)) {
                String[] ps = param.split(OPT_SPLITER);
                fieldName = ps[1];
                optName = ps[0];
            }
            try {
                if (!Strings.isBlank(value) && isColumnField(clazz, fieldName)) {
                	if(CT.equals(optName)){
                    	String[] vs = null;
                    	boolean and = true;
                    	if(value.indexOf("and") > 0){
                    		vs = Strings.splitIgnoreBlank(value, "and");
                    	}else if(value.indexOf("or") > 0){
                    		vs = Strings.splitIgnoreBlank(value, "or");
                    		and = false;
                    	}else if(value.indexOf(" ") > 0){
                    		vs = Strings.splitIgnoreBlank(value, " ");
                    	}else{
                    		vs = new String[]{value.trim()};
                    	}
                    	if(vs != null){
                    		cnd.where().and(Contains.create(fieldName, and, vs));
                    	}
                    	continue;
                	}
                    Field field = Mirror.me(clazz).getField(fieldName);
                    Class<?> fieldType = field.getType();
                    if (isString(field) || isEnum(field)) {
                        if(EQ.equals(optName)){
                            cnd.where().andEquals(fieldName, value);
                        }else if(LK.equals(optName)){
                            cnd.where().andLike(fieldName, value);
                        }else if(IN.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, IN_SPLITER);
                            if(vs.length > 1){
                                cnd.where().andIn(fieldName, vs);
                            }else{
                                cnd.where().andEquals(fieldName, value);
                            }
                        }else if(NE.equals(optName)){
                            cnd.where().andNotEquals(fieldName, value);
                        }else if(NI.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, IN_SPLITER);
                            if(vs.length > 1){
                                cnd.where().andNotIn(fieldName, vs);
                            }else{
                                cnd.where().andNotEquals(fieldName, value);
                            }
                        }else if(NU.equals(optName)){
                            cnd.where().andIsNull(fieldName);
                        }else if(NN.equals(optName)){
                            cnd.where().andNotIsNull(fieldName);
                        }else if(NL.equals(optName)){
                            cnd.where().andNotLike(fieldName, value);
                        }else if(IL.equals(optName)){
                            cnd.where().andLike(fieldName, value, true);
                        }else if(INL.equals(optName)){
                            cnd.where().andNotLike(fieldName, value, true);
                        }
                    } else if (isInt(field)) {
                        if(EQ.equals(optName)){
                            cnd.where().andEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(IN.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, IN_SPLITER);
                            if(vs.length > 1){
                                long[] objs = new long[vs.length];
                                for(int i=0; i<vs.length; i++){
                                    objs[i] = Castors.me().castTo(vs[i], long.class);
                                }
                                cnd.where().andIn(fieldName, objs);
                            }else{
                                cnd.where().andEquals(fieldName, Castors.me().castTo(value, fieldType));
                            }
                        }else if(BT.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, BT_SPLITER);
                            if(vs.length == 2){
                                cnd.where().and(fieldName, "<=", Castors.me().castTo(vs[1], fieldType));
                                cnd.where().and(fieldName, ">=", Castors.me().castTo(vs[0], fieldType));
                            }else{
                                cnd.where().andEquals(fieldName, Castors.me().castTo(vs[0], fieldType));
                            }
                        }else if(GE.equals(optName)){
                            cnd.where().and(fieldName, ">=", Castors.me().castTo(value, fieldType));
                        }else if(LE.equals(optName)){
                            cnd.where().and(fieldName, "<=", Castors.me().castTo(value, fieldType));
                        }else if(GT.equals(optName)){
                            cnd.where().and(fieldName, ">", Castors.me().castTo(value, fieldType));
                        }else if(LT.equals(optName)){
                            cnd.where().and(fieldName, "<", Castors.me().castTo(value, fieldType));
                        }else if(NE.equals(optName)){
                            cnd.where().andNotEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(NI.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, IN_SPLITER);
                            if(vs.length > 1){
                                long[] objs = new long[vs.length];
                                for(int i=0; i<vs.length; i++){
                                    objs[i] = Castors.me().castTo(vs[i], long.class);
                                }
                                cnd.where().andNotIn(fieldName, objs);
                            }else{
                                cnd.where().andNotEquals(fieldName, Castors.me().castTo(value, fieldType));
                            }
                        }else if(NU.equals(optName)){
                            cnd.where().andIsNull(fieldName);
                        }else if(NN.equals(optName)){
                            cnd.where().andNotIsNull(fieldName);
                        }
                    } else if (isDate(field)) {
                        if(EQ.equals(optName)){
                            cnd.where().andEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(BT.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, BT_SPLITER);
                            if(vs.length == 2){
                                cnd.where().and(fieldName, "<=", Castors.me().castTo(vs[1], fieldType));
                                cnd.where().and(fieldName, ">=", Castors.me().castTo(vs[0], fieldType));
                            }else{
                                cnd.where().andEquals(fieldName, Castors.me().castTo(vs[0], fieldType));
                            }
                        }else if(GE.equals(optName)){
                            cnd.where().and(fieldName, ">=", Castors.me().castTo(value, fieldType));
                        }else if(LE.equals(optName)){
                            cnd.where().and(fieldName, "<=", Castors.me().castTo(value, fieldType));
                        }else if(GT.equals(optName)){
                            cnd.where().and(fieldName, ">", Castors.me().castTo(value, fieldType));
                        }else if(LT.equals(optName)){
                            cnd.where().and(fieldName, "<", Castors.me().castTo(value, fieldType));
                        }else if(NE.equals(optName)){
                            cnd.where().andNotEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(NU.equals(optName)){
                            cnd.where().andIsNull(fieldName);
                        }else if(NN.equals(optName)){
                            cnd.where().andNotIsNull(fieldName);
                        }
                    }else if (isFloat(field)) {
                        if(GE.equals(optName)){
                            cnd.where().and(fieldName, ">=", Castors.me().castTo(value, fieldType));
                        }else if(BT.equals(optName)){
                            String[] vs = Strings.splitIgnoreBlank(value, BT_SPLITER);
                            if(vs.length == 2){
                                cnd.where().and(fieldName, "<=", Castors.me().castTo(vs[1], fieldType));
                                cnd.where().and(fieldName, ">=", Castors.me().castTo(vs[0], fieldType));
                            }else{
                                cnd.where().andEquals(fieldName, Castors.me().castTo(vs[0], fieldType));
                            }
                        }else if(LE.equals(optName)){
                            cnd.where().and(fieldName, "<=", Castors.me().castTo(value, fieldType));
                        }else if(GT.equals(optName)){
                            cnd.where().and(fieldName, ">", Castors.me().castTo(value, fieldType));
                        }else if(LT.equals(optName)){
                            cnd.where().and(fieldName, "<", Castors.me().castTo(value, fieldType));
                        }else if(NE.equals(optName)){
                            cnd.where().andNotEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(NU.equals(optName)){
                            cnd.where().andIsNull(fieldName);
                        }else if(NN.equals(optName)){
                            cnd.where().andNotIsNull(fieldName);
                        }else if(EQ.equals(optName)){
                            cnd.where().andEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }
                    } else if (isBool(field)) {
                        if(EQ.equals(optName)){
                            cnd.where().andEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(NE.equals(optName)){
                            cnd.where().andNotEquals(fieldName, Castors.me().castTo(value, fieldType));
                        }else if(NU.equals(optName)){
                            cnd.where().andIsNull(fieldName);
                        }else if(NN.equals(optName)){
                            cnd.where().andNotIsNull(fieldName);
                        }
                    }
                }
            } catch(FailToCastObjectException e){
                cnd.where().and("1", "=", 2);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String order = req.getParameter("order");
        if (Strings.isBlank(order)) {
            order = defaultOrder;
        }
        String sort = req.getParameter("sort");
        if (Strings.isBlank(sort) && isColumnField(clazz, defaultSort)) {
            sort = defaultSort;
        }else if (!isColumnField(clazz, sort)) {
            sort = null;
        }
        
        if (!Strings.isBlank(sort)) {
        	//cnd.resetOrderBy();
            if ("desc".equalsIgnoreCase(order)) {
                cnd.desc(sort);
            } else {
                cnd.asc(sort);
            }
        }
        return cnd;
    }

    private static final String BT = "bt";// between
    private static final String EQ = "eq";// equal
    private static final String NE = "ne";// not equal
    private static final String GE = "ge";// great equal
    private static final String GT = "gt";// great
    private static final String LE = "le";// less equal
    private static final String LT = "lt";// less
    private static final String IN = "in";// in
    private static final String NI = "ni";// not in
    private static final String LK = "lk";// like
    private static final String IL = "il";// ignore case like
    private static final String NL = "nl";// not like
    private static final String INL = "inl";//ignore case not like
    private static final String NU = "nu";// is null
    private static final String NN = "nn";// is not null
    
    private static final String CT = "ct";// contains for mssqlserver full text search only!

    private static final String OPT_SPLITER = "_";
    private static final String IN_SPLITER = ",";
    private static final String BT_SPLITER = "to";

    /** 所有支持的操作 */
    private static Set<String> OPTS = new HashSet<String>();
    static {
        OPTS.add(BT);
        OPTS.add(EQ);
        OPTS.add(NE);
        OPTS.add(GE);
        OPTS.add(GT);
        OPTS.add(LE);
        OPTS.add(LT);
        OPTS.add(IN);
        OPTS.add(NI);
        OPTS.add(LK);
        OPTS.add(IL);
        OPTS.add(NL);
        OPTS.add(INL);
        OPTS.add(NU);
        OPTS.add(NN);
        OPTS.add(CT);
    }
    
    private static final Object LOCK = new Object();
    
    private static Map<String, Set<String>> FIELDS_MAPS = new HashMap<String, Set<String>>();
    
    /**
     * 判断字段是否某个实体类的数据库映射字段
     * @param clazz
     * @param fieldName
     * @return
     */
    private static boolean isColumnField(Class<?> clazz, String fieldName){
    	if("*".equals(fieldName)){
    		return true;
    	}
        Set<String> fields = FIELDS_MAPS.get(clazz.getName());
        if(null == fields){
            synchronized (LOCK) {
                if(null == fields){
                    fields = new HashSet<String>();
                    Field[] fs = Mirror.me(clazz).getFields();
                    for(Field f : fs){
                        if(Clazzs.hasAnnotation(f, Column.class)){
                            fields.add(f.getName());
                        }
                    }
                    FIELDS_MAPS.put(clazz.getName(), fields);
                }
            }
        }
        return fields.contains(fieldName);
    }

    /**
     * 是否带前缀的参数
     * @param param
     * @return
     */
    private static boolean isPrefixedParam(String param) {
        for (String opt : OPTS) {
            if (param.startsWith(opt + OPT_SPLITER) && (param.split(OPT_SPLITER).length == 2)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // 字段类型判断
    // -------------------------------------------------------------------------
    private static boolean isString(Field field) {
        return isType(field, String.class);
    }
    
    private static boolean isEnum(Field field) {
        return field.getType().isEnum();
    }

    private static boolean isDate(Field field) {
        return isType(field, java.sql.Date.class, java.util.Date.class);
    }

    private static boolean isBool(Field field) {
        return isType(field, Boolean.class, boolean.class);
    }

    private static boolean isInt(Field field) {
        return isType(field,
                      Long.class,
                      Integer.class,
                      long.class,
                      int.class,
                      Short.class,
                      short.class,
                      BigInteger.class,
                      Byte.class,
                      byte.class);
    }

    private static boolean isFloat(Field field) {
        return isType(field, Double.class, Float.class, double.class, float.class, BigDecimal.class);
    }

    private static boolean isType(Field field, Class<?>... types) {
        Class<?> clazz = field.getType();
        for (Class<?> type : types) {
            if (type.equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
