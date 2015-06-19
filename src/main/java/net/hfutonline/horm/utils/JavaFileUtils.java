package net.hfutonline.horm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.hfutonline.horm.bean.ColumnInfo;
import net.hfutonline.horm.bean.JavaFieldGetSet;
import net.hfutonline.horm.bean.TableInfo;
import net.hfutonline.horm.core.DBManager;
import net.hfutonline.horm.core.MySQLTypeConvertor;
import net.hfutonline.horm.core.TypeConvertor;

/**
 * 封装了生成Java文件(源代码)常用的操作
 * @author zlb
 *
 */
public class JavaFileUtils {
	/**
	 * 根据字段信息生成java属性信息。如：varchar username-->private String username;以及对应的set和get方法
	 * @param column 字段信息
	 * @param convertor 类型转换器
	 * @return java属性和get、set方法源码
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor){
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		
		String javaFieldType = convertor.databaseType2JavaType(column.getDataType());
		jfgs.setFieldInfo("\tprivate " + javaFieldType + " " + column.getName() + ";\n");
		
		//生成get源码
		//public String getUserName(){
		//	return this.userName;
		//}
		StringBuilder getBuilder = new StringBuilder();
		getBuilder.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2Up(column.getName()) + "(){\n");
		getBuilder.append("\t\treturn this." + column.getName() + ";\n");
		getBuilder.append("\t}\n");
		jfgs.setGetInfo(getBuilder.toString());
		
		//生成set源码
		//public void setUserName(String userName){
		//	this.userName = userName;
		//}
		StringBuilder setBuilder = new StringBuilder();
		setBuilder.append(	"\tpublic void set" + StringUtils.firstChar2Up(column.getName()) + "(");
		setBuilder.append(javaFieldType + " " + column.getName() + "){\n");
		setBuilder.append("\t\tthis." + column.getName() + " = " + column.getName() + ";\n");
		setBuilder.append("\t}\n");
		jfgs.setSetInfo(setBuilder.toString());
		
		return jfgs;
	}
	
	/**
	 * 根据表信息生成java类的源代码
	 * @param tableInfo 表信息
	 * @param convertor 类型转换器
	 * @return java类的源代码
	 */
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor){
		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
		
		for(ColumnInfo column : columns.values()){
			javaFields.add(createFieldGetSetSRC(column, convertor));
		}
		
		StringBuilder src = new StringBuilder();
		
		//生成package语句
		src.append("package " + DBManager.getConf().getPoPackage() + ";\n\n");
		
		//生成import语句
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		
		//生成类声明语句
		src.append("public class " + StringUtils.firstChar2Up(tableInfo.getTname() + " {\n\n"));
		
		//生成属性列表
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getFieldInfo());
		}
		src.append("\n\n");
		
		//生成set方法列表
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getSetInfo());
		}
		
		//生成get方法列表
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getGetInfo());
		}
		
		//生成类结束
		src.append("}\n");
		return src.toString();
	}
	
	/**
	 * 
	 */
	/**
	 * @param tableInfo
	 * @param convertor
	 */
	public static void createJavaPoFile(TableInfo tableInfo, TypeConvertor convertor){
		String className = StringUtils.firstChar2Up(tableInfo.getTname());
		
		String src = createJavaSrc(tableInfo, convertor);
		
		String srcPath = DBManager.getConf().getSrcPath() + "\\";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "\\\\");
		File f = new File(srcPath + packagePath);
		
		if(!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f.getAbsoluteFile() + "/" + className + ".java"));
			writer.write(src);
			writer.close();
			System.out.println(className + "类生成完毕");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ColumnInfo ci = new ColumnInfo("userName", "varchar", 0);
		JavaFieldGetSet jfgs = createFieldGetSetSRC(ci, new MySQLTypeConvertor());
		System.out.println(jfgs.getFieldInfo());
		System.out.println(jfgs.getSetInfo());
		System.out.println(jfgs.getGetInfo());
	}
	
}
