package net.hfutonline.horm.core;

import java.util.List;


public class MySqlQuery<T> extends Query<T> {
	
	@Override
	public List<T> queryPagenate(int pageNum, int size) {
		//TODO 分页功能
		return null;
	}

}
