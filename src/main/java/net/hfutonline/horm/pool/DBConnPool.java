package net.hfutonline.horm.pool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.hfutonline.horm.core.DBManager;

public class DBConnPool {
	/**
	 * 连接池对象
	 */
	private static List<Connection> pool;
	
	/**
	 * 最大连接数
	 * 
	 */
	private static Integer POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
	
	/**
	 * 最小连接数
	 */
	private static Integer POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();
	
	public void initPool(){
		if(pool == null){
			pool = new ArrayList<Connection>();
		}
		while(pool.size() <DBConnPool.POOL_MIN_SIZE){
			pool.add(DBManager.createConn());
		}
	}
	
	public DBConnPool() {
		initPool();
	}
	
	/**
	 * 从连接池中取出一个连接
	 * @return
	 */
	public static synchronized Connection getConn(){
		int lastIndex = pool.size() - 1;
		Connection conn = pool.get(lastIndex);
		
		pool.remove(lastIndex);
		
		return conn;
	}
	
	/**
	 * 将连接放回池中
	 * @param conn
	 */
	public static synchronized void close(Connection conn){
		if(pool.size() > DBConnPool.POOL_MAX_SIZE){
			DBManager.close(conn);
		}
		pool.add(conn);
	}
}
