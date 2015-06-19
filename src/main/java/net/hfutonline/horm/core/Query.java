package net.hfutonline.horm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.hfutonline.horm.bean.ColumnInfo;
import net.hfutonline.horm.bean.TableInfo;
import net.hfutonline.horm.utils.JDBCUtils;
import net.hfutonline.horm.utils.ReflectUtils;

/**
 * 负责查询(对外提供服务的核心类)
 * 
 * @author zlb
 *
 */
public abstract class Query<T> implements Cloneable {
	
	public Object executeQueryTemplate(String sql, Object[] params, Class<T> clazz, CallBack cb){
		Connection conn = DBManager.getConn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			JDBCUtils.handleInsertParams(stmt, params);
			rs = stmt.executeQuery();

			return cb.doExecute(conn, stmt, rs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 直接执行一条DML语句
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return 执行sql影响记录的行数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(sql);

			// 设置参数
			JDBCUtils.handleInsertParams(ps, params);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn);
		}

		return count;
	}

	/**
	 * 将一个对象存储到数据库中
	 * 
	 * @param obj
	 *            要存储的对象
	 */
	@SuppressWarnings("unchecked")
	public void insert(T obj) {
		// obj->表 insert into 表明(字段1，字段2，字段3...) values(？，？，？)；
		Class<T> c = (Class<T>) obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableInfo.getTname() + " (");
		int countNotNull = 0;
		List<Object> params = new ArrayList<Object>();

		Field[] fs = c.getDeclaredFields();
		for (Field f : fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeFieldGetMethod(c, fieldName,
					obj);

			if (fieldValue != null) {
				sql.append(fieldName + ",");
				params.add(fieldValue);
				countNotNull++;
			}
		}
		sql.setCharAt(sql.length() - 1, ')');
		sql.append(" values (");
		for (int i = 0; i < countNotNull; ++i) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length() - 1, ')');

		executeDML(sql.toString(), params.toArray());
	}

	/**
	 * 删除clazz表示类对应的表中的记录(指定主键id的记录)
	 * 
	 * @param clazz
	 *            跟表对应的类的Clazz对象
	 * @param id
	 *            主键的值
	 */
	public void delete(Class<? extends Object> clazz, Object id) {
		// Student.class 1 --> delete from student where id = 2
		// 通过Class对象找tableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		// 获得主键
		ColumnInfo onlyKey = tableInfo.getOnlyPriKey();
		// 封装sql语句
		String sql = "delete from " + tableInfo.getTname() + " where "
				+ onlyKey.getName() + "=?";
		// 执行sql语句
		executeDML(sql, new Object[] { id });
	}

	/**
	 * 删除对象在数据库中对应的记录(对象所在的类对应到表，对象的主键的值对应到记录)
	 * 
	 * @param obj
	 *            要存储的对象
	 */
	public void delete(Object obj) {
		Class<? extends Object> c = obj.getClass();
		TableInfo ti = TableContext.poClassTableMap.get(c);
		ColumnInfo onlyKey = ti.getOnlyPriKey();

		Object priKeyValue = ReflectUtils.invokeFieldGetMethod(c,
				onlyKey.getName(), obj);
		delete(c, priKeyValue);
	}

	/**
	 * 更新指定对应的记录，并且只更新指定的字段的值
	 * 
	 * @param obj
	 *            需要更新的对象
	 * @param fieldName
	 *            待更新的字段的名字
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int update(T obj, String[] fieldNames) {
		// obj{"username", "pwd"} -->update 表明 set uname=?, pwd=> where id=?
		Class<T> c = (Class<T>) obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("update " + tableInfo.getTname()
				+ " set ");
		String priKeyName = tableInfo.getOnlyPriKey().getName();

		for (String fieldName : fieldNames) {
			Object fieldValue = ReflectUtils.invokeFieldGetMethod(c, fieldName,
					obj);
			if (!fieldName.equalsIgnoreCase(priKeyName)) {
				sql.append(fieldName + "=?,");
				params.add(fieldValue);
			}

		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append("where " + priKeyName + "=?");
		params.add(ReflectUtils.invokeFieldGetMethod(c, priKeyName, obj));
		return executeDML(sql.toString(), params.toArray());
	}

	/**
	 * 查询多行记录，并将每行记录封装到clazz指定的类的对象中
	 * 
	 * @param sql
	 *            查询语句
	 * @param clazz
	 *            封装数据的javabean类的class对象
	 * @param params
	 *            sql的参数
	 * @return 查询到的结果
	 */
	public Object queryRows(String sql, Class<T> clazz, Object[] params) {
		return executeQueryTemplate(sql, params, clazz, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement stmt, ResultSet rs) {
				ResultSetMetaData metaData = null;
				try {
					List<T> list = null;
					metaData = rs.getMetaData();
					while (rs.next()) {
						if(list == null){
							list = new ArrayList<T>();
						}
						T rowObject = clazz.newInstance(); // 调用javabean的无参构造器

						// 多列 select username, pwd, age from user where id > ? and age >
						// 18
						for (int i = 0; i < metaData.getColumnCount(); ++i) {
							String columnName = metaData.getColumnLabel(i + 1);
							Object columnValue = rs.getObject(i + 1);
							// 对rowObject设置
							ReflectUtils.invokeFieldSetMethod(rowObject, columnName,
									columnValue);
						}
						list.add(rowObject);
					}
					return list;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null; 
			}
		});
	}

	/**
	 * 查询单行记录，并将每行记录封装到clazz指定的类的对象中
	 * 
	 * @param sql
	 *            查询语句
	 * @param clazz
	 *            封装数据的javabean类的class对象
	 * @param params
	 *            sql的参数
	 * @return 查询到的结果
	 */
	@SuppressWarnings("unchecked")
	public T queryUniqueRow(String sql, Class<T> clazz, Object[] params) {
		List<T> tList = (List<T>) queryRows(sql, clazz, params);
		return (tList == null || tList.size() == 0) ? null : tList.get(0);
	}

	/**
	 * 查询返回一个值
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            sql的参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql, Object[] params) {
		return executeQueryTemplate(sql, params, null, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement stmt,
					ResultSet rs) {
				try {
					while (rs.next()) {
						return rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/**
	 * 查询返回一个数字
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            sql的参数
	 * @return 查询到的结果
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * 分页查询的语句
	 * 
	 * @param pageNum
	 *            当前页
	 * @param size
	 *            每页显示条目
	 * @return 查询的结果集
	 */
	public abstract List<T> queryPagenate(int pageNum, int size);
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
