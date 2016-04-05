package org.araport.image.dao;

import javax.sql.DataSource;

public interface GeneralDao {

	public void executeSQL(String sql);
	
	public void setDataSource(DataSource datasource);
	
}
