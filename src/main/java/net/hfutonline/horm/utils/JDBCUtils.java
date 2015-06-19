package net.hfutonline.horm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了JDBC常用的操作
 * @author zlb
 *
 */
public class JDBCUtils {
	
	public static void handleInsertParams(PreparedStatement ps, Object[] params){
		if(params != null){
			for(int i = 0; i < params.length; ++i){
				try {
					ps.setObject(1 + i, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
