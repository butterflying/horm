package net.hfutonline.horm.core;


/**
 * 负责java数据类型和数据库类型的互相转换
 * @author zlb
 *
 */
public interface TypeConvertor {

	/**
	 * 将数据库类型转化成java的数据类型
	 * @param columnType
	 * @return
	 */
	public String databaseType2JavaType(String columnType);
	
	/**
	 * 将java类型转换成数据库类型
	 * @param javaDataType
	 * @return 数据库类型
	 */
	public String javaType2DatabaseType(String javaDataType);
}
