package net.hfutonline.horm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.hfutonline.horm.bean.ColumnInfo;
import net.hfutonline.horm.bean.TableInfo;
import net.hfutonline.horm.utils.JavaFileUtils;
import net.hfutonline.horm.utils.StringUtils;

/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表生成类对象
 * 
 * @author zlb
 *
 */
@SuppressWarnings("all")
public class TableContext {
	/**
	 * 存储所有的表信息
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

	/**
	 * 将表与Java类结合起来
	 */
	public static Map<Class<Object>, TableInfo> poClassTableMap = new HashMap<Class<Object>, TableInfo>();

	private TableContext() {
	}

	static {
		try {
			Connection conn = DBManager.getConn();
			DatabaseMetaData dbmd = conn.getMetaData();

			ResultSet tableRet = dbmd.getTables(null, "%", "%",
					new String[] { "TABLE" });

			while (tableRet.next()) {
				// 获取表中的信息
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName,
						new HashMap<String, ColumnInfo>(),
						new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);
				// 获取表中所有字段的信息
				ResultSet columnRet = dbmd
						.getColumns(null, "%", tableName, "%");
				while (columnRet.next()) {
					
					ColumnInfo cif = new ColumnInfo(
							columnRet.getString("COLUMN_NAME"),
							columnRet.getString("TYPE_NAME"), 0);
					ti.getColumns().put(columnRet.getString("COLUMN_NAME"), cif);
				}
				
				//获取主键信息
				ResultSet keyRet = dbmd.getPrimaryKeys(null, "%", tableName);
				while(keyRet.next()){
					ColumnInfo ci = ti.getColumns().get(keyRet.getObject("COLUMN_NAME"));
					ci.setKeyType(1);	//设置为主键类型
					ti.getPriKeys().add(ci);
				}
				
				if(ti.getPriKeys().size() > 0){
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//更新表结构
		updateJavaPo();
		
		//加载po包下的类
		loadPOTables();
	}
	
	/**
	 * 生成工程的po类
	 */
	public static void updateJavaPo(){
		Map<String, TableInfo> tables = TableContext.tables;
		TypeConvertor convertor = new MySQLTypeConvertor();
		for(TableInfo t : tables.values()){
			JavaFileUtils.createJavaPoFile(t, convertor);
		}
	}

	/**
	 * 加载po包下面的类
	 */
	public static void loadPOTables(){
		for(TableInfo tableInfo : tables.values()){
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage() + "." + StringUtils.firstChar2Up(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
