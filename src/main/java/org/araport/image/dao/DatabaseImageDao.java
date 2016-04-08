package org.araport.image.dao;

import javax.sql.DataSource;

import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.domain.DbXref;

public interface DatabaseImageDao {

	public boolean create(DatabaseFileImage image);
	
	public boolean update(DatabaseFileImage image);
			
	public void merge(DatabaseFileImage image);
	
	public DbXref mergeAndReturn(DatabaseFileImage image);
	
	public void setDataSource(DataSource datasource);

	
	
}
