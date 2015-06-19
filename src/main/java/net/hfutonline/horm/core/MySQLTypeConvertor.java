package net.hfutonline.horm.core;

/**
 * MySQL数据库的类型转化器
 * 
 * @author zlb
 *
 */
public class MySQLTypeConvertor implements TypeConvertor {

	@Override
	public String databaseType2JavaType(String columnType) {
		if ("VARCHAR".equalsIgnoreCase(columnType)
				|| "char".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("tinyint".equalsIgnoreCase(columnType)
				|| "smallint".equalsIgnoreCase(columnType)
				|| "mediumint".equalsIgnoreCase(columnType)
				|| "int".equalsIgnoreCase(columnType)
				|| "integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		} else if ("double".equalsIgnoreCase(columnType)
				|| "float".equalsIgnoreCase(columnType)) {
			return "Double";
		}
		return null;
	}

	@Override
	public String javaType2DatabaseType(String javaDataType) {
		return null;
	}

}
