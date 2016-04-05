package org.araport.image.dao;

import javax.sql.DataSource;

import org.araport.image.domain.CV;
import org.araport.image.domain.Db;
import org.araport.image.domain.DbXref;

public interface CVDao {

	public boolean create (CV cv);
	public CV findDbByName(String name);
	public boolean save(CV cv);
	
	public void merge(CV cv);
	
	public CV mergeAndReturn(CV cv);
	
	public void setDataSource(DataSource datasource);
	
	
}
