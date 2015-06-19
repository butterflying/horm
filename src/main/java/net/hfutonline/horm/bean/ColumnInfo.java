package net.hfutonline.horm.bean;

/**
 * 封装表中一个字段的信息
 * 
 * @author zlb
 * @version 0.1
 *
 */
public class ColumnInfo {
	/**
	 * 字段名称
	 */
	private String name;
	/**
	 * 字段类型
	 */
	private String dataType;
	/**
	 * 键的类型(普通键，主键，外键)
	 */
	private int keyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}

	public ColumnInfo(String name, String dataType, int keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}

	public ColumnInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ColumnInfo [name=" + name + ", dataType=" + dataType
				+ ", keyType=" + keyType + "]";
	}

}
