package net.hfutonline.horm.utils;

import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 * @author zlb
 *
 */
@SuppressWarnings("all")
public class ReflectUtils {

	public static Object invokeFieldGetMethod(Class c, String fieldName, Object obj){
		try{
			Method m = c.getMethod("get" + StringUtils.firstChar2Up(fieldName), null);
			return m.invoke(obj, null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void invokeFieldSetMethod(Object obj, String fieldName, Object fieldValue){
		try {
			Method method = obj.getClass().getDeclaredMethod("set" + StringUtils.firstChar2Up(fieldName), fieldValue.getClass());
			method.invoke(obj, fieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
