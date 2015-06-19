package net.hfutonline.horm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBack {
	public Object doExecute(Connection conn, PreparedStatement stmt, ResultSet rs);
}
