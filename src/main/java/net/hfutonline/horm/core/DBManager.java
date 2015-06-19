package net.hfutonline.horm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Statement;

import net.hfutonline.horm.bean.Configuration;
import net.hfutonline.horm.pool.DBConnPool;

/**
 * 根据配置信息，维持链接对象的管理
 * 
 * @author zlb
 *
 */
public class DBManager {
	private static Configuration conf;

	static { // 静态代码块
		// 加载指定的资源文件
		Properties props = new Properties();

		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		conf = new Configuration();
		conf.setDriver(props.getProperty("driver"));
		conf.setPoPackage(props.getProperty("poPackage"));
		conf.setPwd(props.getProperty("pwd"));
		conf.setUrl(props.getProperty("url"));
		conf.setSrcPath(props.getProperty("srcPath"));
		conf.setUsingdb(props.getProperty("usingdb"));
		conf.setUser(props.getProperty("user"));
		conf.setQueryClass(props.getProperty("queryClass"));
		conf.setPoolMaxSize(Integer.parseInt(props.getProperty("poolMaxSize")));
		conf.setPoolMinSize(Integer.parseInt(props.getProperty("poolMinSize")));
		
	}
	/**
	 * 创建新的连接对象
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUser(),
					conf.getPwd()); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得数据库连接对象
	 * @return connection
	 */
	public static Connection getConn() {
		return DBConnPool.getConn();
	}
	/**
	 * 关闭数据库链接
	 * @param rs 结果集
	 * @param stmt 查询句柄
	 * @param conn 连接对象
	 */
//	public static void close(ResultSet rs, Statement stmt, Connection conn) {
//		try {
//			if (rs != null) {
//				rs.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					if (conn != null) {
//						conn.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					DBConnPool.close(conn);
				}
			}
		}

	}
	public static void close(Statement stmt, Connection conn) {
		close(null, stmt, conn);
	}
	
	public static void close(Connection conn) {
		close(null, null, conn);
	}
	
	/**
	 * 获得数据库配置对象
	 * @return
	 */
	public static Configuration getConf(){
		return conf;
	}
}
