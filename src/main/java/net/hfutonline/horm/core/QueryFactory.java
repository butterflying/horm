package net.hfutonline.horm.core;

/**
 * 创建Query对象的工厂类
 * 
 * @author zlb
 *
 */
@SuppressWarnings("all")
public class QueryFactory {
	private static Query prototype;	//原型对象
	static {
		String className = DBManager.getConf().getQueryClass();
		try {
			Class c = Class.forName(className);
			prototype = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private QueryFactory() {// 私有构造器
	}

	public static Query createQuery() {
		try {
			return (Query) prototype.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
