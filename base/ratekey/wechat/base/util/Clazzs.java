package ratekey.wechat.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Mirror;
import org.nutz.resource.Scans;

public class Clazzs {

    /**
     * 查找某个类所在包的子包下具有某个Annotation的类
     * 
     * @param clazz
     * @param anno
     * @return
     */
    public static List<Class<?>> findAnnotatedClass(Class<?> clazz, Class<? extends Annotation> anno) {
        return findAnnotatedClass(clazz.getPackage().getName(), anno);
    }

    /**
     * 查找某个包下所有具有某个Annotation的类
     * 
     * @param pkg
     * @param anno
     * @return
     */
    public static List<Class<?>> findAnnotatedClass(String pkg, Class<? extends Annotation> anno) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        for (Class<?> clazz : Scans.me().scanPackage(pkg)) {
            if (hasAnnotation(clazz, anno)) {
                list.add(clazz);
            }
        }
        return list;
    }

    /**
     * 查找某个类所在包的所有子包下具有某个接口的实现类
     * 
     * @param clazz
     * @param face
     * @return
     */
    public static List<Class<?>> findImplements(Class<?> clazz, Class<?> face) {
        return findImplements(clazz.getPackage().getName(), face);
    }

    /**
     * 查找某个包下某个接口的所有实现类
     * 
     * @param pkg
     * @param face
     * @return
     */
    public static List<Class<?>> findImplements(String pkg, Class<?> face) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        for (Class<?> clazz : Scans.me().scanPackage(pkg)) {
            Mirror<?> mirror = Mirror.me(clazz);
            if (!mirror.isInterface() && mirror.isOf(face)) {
                list.add(clazz);
            }
        }
        return list;
    }

    /**
     * 判断某个类是否具有某个接口
     * 
     * @param clazz
     * @param face
     * @return
     */
    public static boolean hasInterface(Class<?> clazz, Class<?> face) {
        Mirror<?> mirror = Mirror.me(clazz);
        return mirror.isOf(face);
    }

    /**
     * 判断某个类是否具有某个Annotation
     * 
     * @param clazz
     * @param anno
     * @return
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> anno) {
        return (clazz.getAnnotation(anno) != null);
    }

    /**
     * 判断某个字段是否具有某个Annotation
     * 
     * @param clazz
     * @param anno
     * @return
     */
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> anno) {
        return (field.getAnnotation(anno) != null);
    }

    /**
     * 判断某个方法是否具有某个Annotation
     * 
     * @param clazz
     * @param anno
     * @return
     */
    public static boolean hasAnnotation(Method method, Class<? extends Annotation> anno) {
        return (method.getAnnotation(anno) != null);
    }
}
