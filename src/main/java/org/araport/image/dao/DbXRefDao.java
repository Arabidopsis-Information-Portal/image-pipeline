package org.araport.image.dao;

import javax.sql.DataSource;

import org.araport.image.domain.DbXref;

public interface DbXRefDao {
		
	public boolean create(DbXref dbXref);
	
	public boolean update(DbXref dbXref);
	
	public DbXref findDbXrefByAccessionAndDb(int dbId, String accession);
	
	public void merge(DbXref dbXref);
	
	public DbXref mergeAndReturn(DbXref dbXref);
	
	public void setDataSource(DataSource datasource);

}
