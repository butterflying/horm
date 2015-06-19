package net.hfutonline.horm.utils;

/**
 * 封装了字符串常用的操作
 * @author zlb
 *
 */
public class StringUtils {
	
	/**
	 * 将目标首字母变成大写
	 * @param str 目标字符串
	 * @return 首字母变成大写的字符串
	 */
	public static String firstChar2Up(String str){
		//abcd->Abcd
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
}
